
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolments.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentDeleteTest extends TestHarness {

	@Autowired
	protected StudentEnrolmentTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String course) {

		super.signIn("student1", "student1");
		super.clickOnMenu("Student", "My enrolments");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("coursesRead", course);
		super.clickOnSubmit("Delete");

		super.checkListingExists();
		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it
		// HINT+ doesn't involve any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a lecture using principals with
		// HINT+ inappropriate roles.

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findEnrolemntByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments) {
			param = String.format("id=%d", enrolment.getId());

			super.checkLinkExists("Sign in");
			super.request("/student/enrolment/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/student/enrolment/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student2", "student2");
			super.request("/student/enrolment/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/student/enrolment/delete", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
