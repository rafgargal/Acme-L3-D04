
package acme.features.student.activity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.enrolments.Enrolment;
import acme.features.student.enrolment.StudentEnrolmentRepository;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityListService extends AbstractService<Student, Activity> {

	@Autowired
	protected StudentEnrolmentRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		Enrolment object;
		Principal principal;
		int enrolmentId;

		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		object = this.repository.findEnrolmentById(enrolmentId);
		principal = super.getRequest().getPrincipal();

		status = object.getStudent().getId() == principal.getActiveRoleId() && object.isDraftMode() == false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Activity> objects;
		Principal principal;
		int enrolmentId;
		Enrolment enrolment;

		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);

		principal = super.getRequest().getPrincipal();
		objects = this.repository.findActivitiesByEnrolmentId(enrolmentId);

		super.getBuffer().setData(objects);
		super.getResponse().setGlobal("enrolmentId", enrolmentId);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		int enrolmentId;

		Tuple tuple;

		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		tuple = super.unbind(object, "title", "summary", "activityType", "moreInfo");

		tuple.put("enrolmentId", enrolmentId);

		super.getResponse().setData(tuple);
	}
}
