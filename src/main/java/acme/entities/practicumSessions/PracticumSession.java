
package acme.entities.practicumSessions;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.practicum.Practicum;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PracticumSession extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	//Attributes

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			summary;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				endDate;

	@URL
	protected String			moreInfoLink;

	protected boolean			addendum;

	//Derived attributes

	//Relationship

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Practicum			practicum;
}
