
package acme.features.student.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentCourseShowService extends AbstractService<Student, Course> {

	@Autowired
	protected StudentCourseRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Student.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Tuple tuple;

		int id;

		id = super.getRequest().getData("id", int.class);
		final String lecturer = object.getLecturer().getUserAccount().getUsername();

		tuple = super.unbind(object, "code", "title", "cAbstract", "retailPrice", "furtherInfo");
		tuple.put("lecturers", lecturer);

		super.getResponse().setData(tuple);
	}

}
