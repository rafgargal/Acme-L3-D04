
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSessions.PracticumSession;
import acme.features.company.practicumSession.CompanyPracticumSessionRepository;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumPublishService extends AbstractService<Company, Practicum> {

	@Autowired
	protected CompanyPracticumRepository		practicumRepository;
	@Autowired
	protected CompanyPracticumSessionRepository	practicumSessionRepository;


	@Override
	public void check() {

		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);

	}
	@Override
	public void authorise() {
		final boolean status;
		Practicum practicum;
		final Principal principal;
		int practicumId;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.practicumRepository.findPracticumById(practicumId);
		principal = super.getRequest().getPrincipal();

		status = practicum.getCompany().getId() == principal.getActiveRoleId() && practicum.getDraftMode();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Practicum practicum;
		int id;

		id = super.getRequest().getData("id", int.class);
		practicum = this.practicumRepository.findPracticumById(id);

		super.getBuffer().setData(practicum);

	}

	@Override
	public void bind(final Practicum practicum) {
		assert practicum != null;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.practicumRepository.findCourseById(courseId);

		super.bind(practicum, "code", "title", "summary", "goals");
		practicum.setCourse(course);
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;
		Collection<PracticumSession> sessions;
		sessions = this.practicumSessionRepository.findPracticumSessionsByPracticumId(object.getId());

		super.state(sessions.size() != 0, "*", "company.practicum.error.label.empty");

	}

	@Override
	public void perform(final Practicum practicum) {
		assert practicum != null;

		practicum.setDraftMode(false);

		this.practicumRepository.save(practicum);
	}
	@Override
	public void unbind(final Practicum practicum) {
		assert practicum != null;
		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.practicumRepository.findAllCourses();
		choices = SelectChoices.from(courses, "code", practicum.getCourse());

		tuple = super.unbind(practicum, "code", "title", "summary", "goals", "draftMode");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);

	}

}
