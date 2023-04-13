
package acme.features.company.practicumSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSessions.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	@Autowired
	protected CompanyPracticumSessionRepository repository;


	@Override
	public void check() {

		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}
	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		PracticumSession session;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);

		session = new PracticumSession();
		session.setPracticum(practicum);

		super.getBuffer().setData(session);

	}

	@Override
	public void bind(final PracticumSession session) {

		assert session != null;

		super.bind(session, "title", "summary", "startDate", "endDate", "moreInfoLink");

	}

	@Override
	public void validate(final PracticumSession session) {
		assert session != null;
	}

	@Override
	public void perform(final PracticumSession session) {
		assert session != null;
		this.repository.save(session);
	}
	@Override
	public void unbind(final PracticumSession session) {
		assert session != null;

		Tuple tuple;

		tuple = super.unbind(session, "title", "summary", "startDate", "endDate", "moreInfoLink");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().setData(tuple);
	}
}
