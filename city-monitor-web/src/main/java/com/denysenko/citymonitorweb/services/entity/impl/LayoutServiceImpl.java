package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.dto.LayoutPreviewDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.repositories.hibernate.LayoutRepository;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.converters.impl.LayoutEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LayoutServiceImpl implements LayoutService {

    private final LayoutRepository layoutRepository;
    private final QuizRepository quizRepository;
    private final LayoutEntityToDTOConverter layoutEntityToDTOConverter;

    @Override
    public LayoutDTO getLayoutDTOById(Long id) {
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти макет з id = " + id));
        return layoutEntityToDTOConverter.convertEntityToDTO(layout);
    }

    @Override
    public Layout getFullLayoutById(Long id) {
        return layoutRepository.getLayoutWithPolygonsBy(id);
    }

    @Transactional
    public void saveLayout(Layout layout) {
          layoutRepository.save(layout);
    }

    @Transactional
    public void saveLayout(LayoutDTO layoutDTO) {
        Layout layout = layoutEntityToDTOConverter.convertDTOToEntity(layoutDTO);
        layoutRepository.save(layout);
    }

    @Override
    public Page<LayoutPreviewDTO> getPageOfLayouts(int pageNumber, int size) {
        if (pageNumber < 1 || size < 1)
            throw new IllegalArgumentException("Номер сторінки та її розмір має бути більше нуля." +
                    " Поточні значення: pageNumber = " + pageNumber + ", size = " + size);

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<LayoutPreviewDTO> layoutPage = layoutRepository.findAllPreviewsBy(request);
        return layoutPage;
    }

    @Override
    public List<LayoutPreviewDTO> getNotDeprecatedLayoutsPreviews() {
        return layoutRepository.findPreviewsByStatusNot(LayoutStatus.DEPRECATED);
    }

    @Transactional
    @Override
    public void deleteLayout(Long id) {
        layoutRepository.deleteById(id);
    }

    @Transactional
    public LayoutStatus markLayoutAsDeprecated(Long layoutId) {
        setLayoutStatusById(layoutId, LayoutStatus.DEPRECATED);
        return LayoutStatus.DEPRECATED;
    }

    @Transactional
    public LayoutStatus markLayoutAsActual(Long layoutId) {
        LayoutStatus newStatus;
        if (quizRepository.existsByLayoutId(layoutId)) {
            setLayoutStatusById(layoutId, LayoutStatus.IN_USE);
            newStatus = LayoutStatus.IN_USE;
        } else {
            setLayoutStatusById(layoutId, LayoutStatus.AVAILABLE);
            newStatus = LayoutStatus.AVAILABLE;
        }
        return newStatus;
    }

    private void setLayoutStatusById(Long id, LayoutStatus layoutStatus) {
        Optional<Layout> layoutOptional = layoutRepository.findById(id);
        layoutOptional.ifPresentOrElse(l -> l.setStatus(layoutStatus),
                () -> new EntityNotFoundException("Не вдалось знайти макет з id = " + id));
    }

    public LayoutStatus getStatusById(Long id) {
        return layoutRepository.getLayoutStatusById(id);
    }

}
