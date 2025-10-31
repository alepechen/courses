package com.example.course.spec;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseCategory;
import com.example.course.dto.CourseFilterDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CourseSpecification {
    public static Specification<Course> filter(CourseFilterDto request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getCategory())) {
                CourseCategory category = CourseCategory.valueOf(request.getCategory());
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (StringUtils.hasText(request.getAuthor())) {
                predicates.add(cb.equal(root.get("author"), request.getAuthor()));
            }
            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
