
package acme.features.authenticated.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class AuthenticatedCompanyCreateService extends AbstractService<Authenticated, Company> {

	@Autowired
	protected AuthenticatedCompanyRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Company.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Company object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Company();
		object.setUserAccount(userAccount);

		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final Company company) {
		assert company != null;

		super.bind(company, "name", "VATnumber", "summary", "moreInfoLink");
	}

	@Override
	public void validate(final Company company) {
		assert company != null;

		if (!super.getBuffer().getErrors().hasErrors("VATnumber")) {
			Company existing;

			existing = this.repository.findCompanyByVATnumber(company.getVATnumber());
			super.state(existing == null, "VATnumber", "company.practicum.error.label.VATnumber");
		}
	}

	@Override
	public void perform(final Company company) {
		assert company != null;
		this.repository.save(company);
	}

	@Override
	public void unbind(final Company company) {
		assert company != null;

		Tuple tuple;

		tuple = super.unbind(company, "name", "VATnumber", "summary", "moreInfoLink");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
