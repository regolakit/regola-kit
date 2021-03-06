package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.LessThan;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;

public class LessThanHandler extends AbstractAnnotationHandler {

	public LessThanHandler() {
		super(LessThan.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((LessThan) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		criteria.add(Restrictions.lt(propertyPath, filterValue));
	}
}
