package com.epam.esm.service.impl;


import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.tag.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    private final String DEFAULT_NAME = "Tag";

    private final long DEFAULT_ID = 1;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findTagByIdTest() {
        Tag tag = new Tag(DEFAULT_ID, DEFAULT_NAME);

        Mockito.when(tagRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(tag));
        Tag resultTag = tagService.getTagById(DEFAULT_ID);

        assertEquals(tag, resultTag);
    }

    @Test
    void findAllTagsTest() {
        Tag tag = new Tag(DEFAULT_NAME);
        List<Tag> allTags = Collections.singletonList(tag);
        List<Tag> expectedTags = Collections.singletonList(tag);

        Pageable pageable = PageRequest.of(0,10);

        Mockito.when(tagRepository.findAll(pageable)).thenReturn(new PageImpl<>(allTags));
        List<Tag> resultTagList = tagService.getAllTags(pageable);

        assertEquals(expectedTags, resultTagList);
    }

    @Test
    void deleteTagTest() {
        Tag tag = Tag.builder()
                .id(DEFAULT_ID)
                .tagName(DEFAULT_NAME)
                .build();
        Mockito.when(tagRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(tag));
        Assertions.assertEquals(Boolean.TRUE, tagService.deleteTagById(DEFAULT_ID));
    }

    @Test
    void createTagTest() {
        Tag tagForCreating = new Tag(DEFAULT_NAME);
        Tag createdTag = new Tag(DEFAULT_ID, DEFAULT_NAME);

        Mockito.when(tagRepository.save(tagForCreating)).thenReturn(createdTag);
        Tag resultTag = tagService.createNewTag(tagForCreating);

        assertEquals(createdTag, resultTag);
    }

    @Test
    void createTagTestShouldThrowException() {
        Tag tagForCreating = new Tag(DEFAULT_NAME);
        Tag existingTag = new Tag(DEFAULT_ID, DEFAULT_NAME);

        Mockito.when(tagRepository.findByTagName(DEFAULT_NAME)).thenReturn(Optional.of(existingTag));

        assertThrows(ApplicationException.class, () -> tagService.createNewTag(tagForCreating));
    }

    @Test
    void deleteTagTestShouldThrowException() {
        int id = 99;
        assertThrows(ApplicationException.class, () -> tagService.deleteTagById(id));
    }

    @Test
    void GetFullDataTagsTest() {
        List<Tag> tags = List.of(new Tag(DEFAULT_NAME));

        Mockito.when(tagRepository.findByTagName(Mockito.anyString())).thenReturn(
                Optional.of(Tag.builder()
                .tagName(DEFAULT_NAME)
                .build()));
        List<Tag> getTags = tagService.getFullTagsData(tags);
        assertEquals(tags.size(), getTags.size());
    }


}
