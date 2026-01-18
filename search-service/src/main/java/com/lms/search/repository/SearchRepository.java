//package com.lms.search.repository;
//
//import com.lms.search.model.SearchIndex;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface SearchRepository extends JpaRepository<SearchIndex, Long> {
//
//    List<SearchIndex> findByCourseTitleContainingIgnoreCase(String keyword);
//
//}

package com.lms.search.repository;

import com.lms.search.model.SearchIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<SearchIndex, Long> {

    List<SearchIndex> findByCourseTitleContainingIgnoreCaseOrCourseDescriptionContainingIgnoreCase(
            String keyword,
            String keyword2
    );
}
