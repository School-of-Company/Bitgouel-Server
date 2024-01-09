package team.msg.domain.withdrow.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import team.msg.domain.student.model.Student

@Entity
class WithdrawStudent(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdraw_user_id",nullable = false)
    var id: Long = 0,

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    val student: Student
)