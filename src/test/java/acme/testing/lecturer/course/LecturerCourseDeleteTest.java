
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerCourseDeleteTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected LecturerCourseTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title) {
		// HINT: this test authenticates as an lecturer and checks that he or
		// HINT+ she can delete the expected courses.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a course with a role other than "Lecturer".

		Collection<Course> courses;
		String params;

		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				params = String.format("id=%d", course.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/course/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/course/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to delete a published course that was registered by the principal.

		Collection<Course> courses;
		String params;

		super.signIn("lecturer2", "lecturer2");
		courses = this.repository.findManyCoursesByLecturerUsername("lecturer2");
		for (final Course course : courses)
			if (!course.isDraftMode()) {
				params = String.format("id=%d", course.getId());
				super.request("/lecturer/course/delete", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to delete a course that wasn't registered by the principal.

		Collection<Course> courses;
		String params;

		super.signIn("lecturer2", "lecturer2");
		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses) {
			params = String.format("id=%d", course.getId());
			super.request("/lecturer/course/delete", params);
		}
		super.signOut();
	}

}
