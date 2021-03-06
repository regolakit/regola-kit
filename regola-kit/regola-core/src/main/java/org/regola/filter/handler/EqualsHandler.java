package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.Equals;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;

public class EqualsHandler extends AbstractAnnotationHandler {

	public EqualsHandler() {
		super(Equals.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((Equals) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		criteria.add(Restrictions.eq(propertyPath, filterValue));
	}
}
