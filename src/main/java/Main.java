import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.action.internal.CollectionUpdateAction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();

//        Course course = session.get(Course.class, 1);
//        System.out.println(course.getTeacher().getName());
//        List<Student> studentList = course.getStudents();
//        for (Student student: studentList) System.out.println(student.getName());

        Query query = session.createQuery("Select count(*) from Purchase");
        Long count = (long) query.list().get(0);

        for (int i = 1; i <= count; i++) {
            session.beginTransaction();
            Purchase purchase = (Purchase) session.createQuery("from Purchase where id = :i")
                    .setParameter("i", i).list().get(0);
            Course course = (Course) session.createQuery("from Course where name = :name")
                    .setParameter("name", purchase.getCourseName()).list().get(0);
            Student student = (Student)  session.createQuery("from Student where name =:name")
                    .setParameter("name", purchase.getStudentName()).list().get(0);
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setCourseId(course.getId());
            studentCourse.setStudentId(student.getId());

            session.save(studentCourse);

            session.getTransaction().commit();


        }

        System.out.println();

        session.close();
        sessionFactory.close();

    }
}
