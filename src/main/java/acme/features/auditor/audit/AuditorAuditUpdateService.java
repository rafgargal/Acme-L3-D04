
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditing.Audit;
import acme.entities.course.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditUpdateService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
		final int auditId = super.getRequest().getData("id", int.class);
		final Audit audit = this.repository.findOneAuditById(auditId);

		final int auditorIdFromTutorial = audit.getAuditor().getId();

		final int auditorIdFromLoggedUser = super.getRequest().getPrincipal().getActiveRoleId();

		super.getResponse().setAuthorised(auditorIdFromTutorial == auditorIdFromLoggedUser && audit.getDraftMode());

	}

	@Override
	public void load() {
		Audit object;
		int courseId;

		courseId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(courseId);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		super.bind(object, "code", "conclusion", "weakPoints", "strongPoints", "mark", "draftMode");
		object.setCourse(course);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;
		/*
		 * if (!super.getBuffer().getErrors().hasErrors("code")) {
		 * Audit existing;
		 * 
		 * existing = this.repository.findOneAuditByCode(object.getCode());
		 * super.state(existing == null, "code", "lecturer.audit.form.error.duplicated");
		 * 
		 * }
		 */

	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "lecturer.audit.form.error.duplicated");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
