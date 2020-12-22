package gmc.rd.report.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment
	private int id;
	
	@Column(nullable = false, length=100)
	private String title;
	
	@OneToOne
	@JoinColumn(name="boardId")
	private Board board;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@OneToMany
	@JoinColumn(name = "replyId")
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp timestamp;
	
}
