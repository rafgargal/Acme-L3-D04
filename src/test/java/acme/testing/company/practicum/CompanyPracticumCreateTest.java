
package acme.testing.company.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class CompanyPracticumCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String summary, final String goals, final String course) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 1, title);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("summary", summary);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);

		super.clickOnButton("Practicum Sessions");
		super.checkListingExists();
		super.checkListingEmpty();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String summary, final String goals) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("goals", goals);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();

	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/company/practicum/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();
	}

}
