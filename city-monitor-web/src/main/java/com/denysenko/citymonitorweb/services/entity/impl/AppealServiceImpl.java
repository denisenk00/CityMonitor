package com.denysenko.citymonitorweb.services.entity.impl;


import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.exceptions.DownloadTelegramFileException;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import com.denysenko.citymonitorweb.models.entities.AppealFile;
import com.denysenko.citymonitorweb.repositories.hibernate.AppealFileRepository;
import com.denysenko.citymonitorweb.repositories.hibernate.AppealRepository;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final AppealFileRepository appealFileRepository;
    private final TelegramService telegramService;

    @Override
    public long countOfUnreadAppeals(){
        return appealRepository.countByStatusEquals(AppealStatus.UNREAD);
    }

    @Override
    public Page<AppealDTO> getPageByStatuses(int pageNumber, int size, Set<AppealStatus> statuses){
        if(pageNumber < 1 || size < 1) throw new IllegalArgumentException("Номер сторінки та її розмір має бути більше нуля. Поточні значення: pageNumber = " + pageNumber + ", size = " + size);

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "postDate"));
        Page<AppealDTO> appealsPage = appealRepository.findAllByStatusIn(statuses, request);
        return appealsPage;
    }

    @Transactional
    @Override
    public void updateStatusById(Long id, AppealStatus appealStatus){
        Optional<Appeal> appeal = appealRepository.findById(id);
        appeal.ifPresentOrElse((a)-> {
            a.setStatus(appealStatus);
        }, () -> new EntityNotFoundException("Не вдалось знайти звернення з id = " + id));
    }

    public AbstractMap.SimpleImmutableEntry<String, byte[]> getAppealFileContent(Long fileId){
        FileInputStream fileContent;
        AppealFile file;
        byte[] resource;

        try {
            file = appealFileRepository.findById(fileId).orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти файл звернення з id = " + fileId));
            fileContent = telegramService.getFileByID(file.getTgFileId());
            resource = fileContent.readAllBytes();
        } catch (TelegramApiException e) {
            String reason = "Помилка при надсиланні запиту на завантаження файлу до Telegram API";
            throw new DownloadTelegramFileException(reason, e, fileId.toString());
        } catch (IOException e) {
            throw new RuntimeException("Не вдалось завантажити вміст файлу", e);
        }
        return new AbstractMap.SimpleImmutableEntry<>(file.getName(), resource);
    }


}
