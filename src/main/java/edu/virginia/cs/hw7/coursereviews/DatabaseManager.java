package edu.virginia.cs.hw7.coursereviews;

import java.sql.*;

public class DatabaseManager {
    public static final String DB_NAME = "Reviews.sqlite3";
    Connection connection;

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ClassCastException("Database not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConnection() {
        if (connection == null) {
            throw new RuntimeException("Not connected to database");
        }
    }

    private boolean doesTableExist(String tableName) {
        checkConnection();
        try {
            return connection.getMetaData().getTables(null, null, tableName, null).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTables() {
        checkConnection();
        try {
            Statement statement = connection.createStatement();
            String createStudentsTable = "CREATE TABLE IF NOT EXISTS students ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT NOT NULL,"
                    + "PASSWORD TEXT NOT NULL)";
            statement.executeUpdate(createStudentsTable);

            String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "DEPARTMENT TEXT NOT NULL,"
                    + "CATALOG_NUMBER INTEGER NOT NULL)";
            statement.executeUpdate(createCoursesTable);

            String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "STUDENT_ID INTEGER NOT NULL,"
                    + "COURSE_ID INTEGER NOT NULL,"
                    + "RATING INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),"
                    + "COMMENT TEXT NOT NULL,"
                    + "FOREIGN KEY(STUDENT_ID) REFERENCES students(ID) ON DELETE CASCADE,"
                    + "FOREIGN KEY(COURSE_ID) REFERENCES courses(ID) ON DELETE CASCADE)";
            statement.executeUpdate(createReviewsTable);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void populateDatabase() {
        DatabaseManager db = new DatabaseManager();
        db.connect();

        CourseManager courseManager = new CourseManager(db);
        StudentManager studentManager = new StudentManager(db);
        ReviewsManager reviewsManager = new ReviewsManager(db);

        // Clear database
        reviewsManager.clearReviews();
        studentManager.clearStudents();
        courseManager.clearCourses();

        // Add courses
        Course cs2100 = new Course("CS", 2100);
        Course cs2110 = new Course("CS", 2110);
        Course cs2150 = new Course("CS", 2150);
        courseManager.addCourse(cs2100);
        courseManager.addCourse(cs2110);
        courseManager.addCourse(cs2150);

        // Add students
        Student alice = new Student("Alice", "alice123");
        Student bob = new Student("Bob", "bob456");
        Student charlie = new Student("Charlie", "charlie789");
        Student brandon = new Student("Brandon", "brandon123");
        Student dave = new Student("Dave", "dave456");
        Student edward = new Student("Edward", "edward789");
        Student frank = new Student("Frank", "frank123");
        Student greg = new Student("Greg", "greg456");
        Student harry = new Student("Harry", "harry789");
        Student ian = new Student("Ian", "ian123");

        studentManager.addStudent(alice);
        studentManager.addStudent(bob);
        studentManager.addStudent(charlie);
        studentManager.addStudent(brandon);
        studentManager.addStudent(dave);
        studentManager.addStudent(edward);
        studentManager.addStudent(frank);
        studentManager.addStudent(greg);
        studentManager.addStudent(harry);
        studentManager.addStudent(ian);


        // Add reviews
        Review review1 = new Review(alice, cs2100, "Great course, highly recommend!", 5);
        Review review2 = new Review(bob, cs2100, "Not too bad, but not great either.", 3);
        Review review3 = new Review(charlie, cs2100, "Terrible course, do not take!", 1);
        Review review4 = new Review(alice, cs2110, "Very challenging, but learned a lot.", 4);
        Review review5 = new Review(bob, cs2110, "Lectures were boring, but assignments were interesting.", 3);
        Review review6 = new Review(charlie, cs2150, "*Unpopular Opinions of CS 2150, a la FAQ. Read the other reviews for popular opinions!* -> On a scale from Dudley to Voldemort, how hard is it to best this course (get an A or A+)? I'd say it's about the equivalent of vying for a golden egg from the Hungarian Horntail in the 1994 Triwizard Tournament. It seems intimidating, but is actually quite manageable if you can use your resources effectively (your broomstick). While I agree with other reviews that the course requires effort to get a decent grade in, I don't think it is an absurd/unfairly difficult class. In fact, it was by far my favorite class during the semester I took it; all of my other classes were not nearly as exciting or fun as CS 2150. The plurality grade of the class is an A, and Bloomfield typically gives the top 3-5% of students an A+. -> Would Kylo Ren let the labs/exams stand in his way? The lab assignments may seem daunting, but they are thoroughly documented and if you use office hours/Piazza/stack overflow, they are much more doable. The exams are similarly straightforward if you take the time to review the past 3 or 4 instances of the exam and know them well. Lab 6 (word puzzle search) and lab 10 (Huffman encoder/decoder) are non-trivial labs; they are not necessarily difficult, they just take longer than most other labs. Optimizing the word search was one of my favorite memories of the class, as it was the first time I have been asked by a Professor to make a program run as fast as possible. Kylo Ren would probably rage-quit the second lab and not finish what Darth Vader started. -> How can I exploit the course and reach M4X1MUM P0W3R? - Actually use Piazza, even if you're too cool for it; you'll be greatly rewarded for posting good answers, followup discussions, and questions at the end of the semester with your participation grade. I received an obscenely high participation grade (you can go well above 100%) by doing this and the impact on my overall grade was significant. - Contribute to the GitHub repository with bug fixes and typo fixes to further boost your participation grade. Every point counts! - Start the lab assignments on Friday/Saturday and get any help you need later from Piazza and office hours on Sunday/Monday while everyone else is just beginning the assignment. This strategy especially helps with the non-trivial lab assignments (6 and 10). - Have fun, it's over before you know it :'( TL;DR harry potter, star wars, science?", 1);
        Review review7 = new Review(alice, cs2150, "CS 2150 is the best class I have ever taken at UVA. Here's why: 1. The professor (Aaron Bloomfield) is amazing at teaching the course. He knows all the material inside out and can answer any of your questions. He's been teaching the course for like 10 years now, and has developed a very solid curriculum. He also makes the lectures interesting and keeps you engaged. 2. The course is just so well designed. Everything you need is laid out for you, grading is clear, you get to see ALL past exams to prepare adequately. Basically, what is expected of you is told to you. The main problem students have with the course is simply not putting in enough time. That is why the GPA for this course is so low (At least I believe). I think 75% of students can get an A or A- in the class, but that requires at least 10 hours of work every week. Since most kids don't put in that much time, their grade in the class suffers. 3. You figure out if you really like CS. Every week you're doing a lab; there's almost no break between labs and learning something new. That being said, this tells people if they really like CS or really hate it. For example, one of my friends could not stand the class and decided to quit CS, but I personally found myself to like it even more. The class really teaches the foundation of data structures and the \"core\" concepts that I think every aspiring CS major should understand. Tips to succeed in this class: -Go to lecture! Bloomfield makes it worthwhile to go -If you signed up for Mark Floryan's section (sometime he teaches the course as well), try to go to Bloomfield's lecture. I found Bloomfield to be much more knowledgable about all the topics. Floryan doesn't usually teach the course so he tends to be rusty on a lot of topics and often could not answer questions that we asked of him. -Ideally have some friends in the class that you can collaborate with on the labs. As long as you're not directly comparing code you can work with friends on the lab - share ideas, high-level implementation, etc. This is really helpful as multiple streams of ideas helps when some of the labs can be tough. -If you're on the waitlist, don't give up as usually many people drop the class after the first lab. Even the professors scare you and act as if you probably won't get into the class. But every semester 99% of the kids who want to get into the class get in. You (most likely) will get into the class.", 1);
        Review review8 = new Review(bob, cs2150, "I heard horrible things about the difficulty of this class, so I went into it expecting to need to work hard to do well. The class is difficult, but mostly because of the time required to complete assignments. Expect to spend 10-15 hours on some labs each week (2, 5, and 6 are especially horrible). Despite the time commitment, if you are willing to work hard, you will do well. The exam average is low, but I didn't find the exams to be too challenging, as they are predictable. Also, the course is curved. Unlike 2110 and 2102, you will ACTUALLY learn in this class, and you will feel like a REAL computer science major. I have a love-hate relationship with this class, as all computer science majors do, but overall I expect this class to be one of the most rewarding and applicable courses I take at UVA. At the same time, I cant wait for this class its over.", 1);
        Review review9 = new Review(brandon, cs2150, "This course is honestly ridiculous. Tests count for about twice as much of your total grade as they realistically should, given that the material presented on them is completely random and generally not reflective of what you've been spending every waking hour learning through the homework. Bloomfield throws you in the deep end of the pool with no swimming experience the second week with the linkedlist lab. An absurdly difficult 3-part Huffman coding lab is assigned over Thanksgiving break with no access to TA office hours. Lab instructions take forever to understand because they skip all over the place like they were written by a crackhead. Bloomfield is indeed a \"nice guy\" to talk to in person, and his lectures (for being CS lectures) are paced well and entertaining enough. But that isn't nearly enough to make up for the fact that the tests are absolutely the worst (not just hardest; worst in terms of testing how well you've gotten what you need from the course) I have encountered at UVA, and they count for 55% of the course grade! All those hours I spent essentially teaching myself data structures and C++ to complete the labs went to waste, because I still got a bad grade in the course due to terrible grades on the god-awful exams. I went to plenty of office hours and went to the review sessions for the midterms, with little to show for it in my exam grade.", 3);
        Review review10 = new Review(dave, cs2150, "You are not set up to succeed in this course. I would strongly recommend avoiding this class if you do not need it. Bloomfield does not give you the prep you need to understand the labs so you will spend a ton of time outside of class looking for help. It is so many hours per week and unfortunately only 3 credits. The TAs will tell you that you can figure it out in OH if you are super lost too. He makes the exams really hard and generally doesn't care how his students are doing. If he was even available at all for feedback he would hear this general sentiment (he doesn't respond to emails by the way).", 4);
        Review review11 = new Review(edward, cs2150, "First, I don't understand how professors can get away with not checking their email just because they get too much email. You're here to teach and support students. If students can't get a hold of you through email, you are failing. It's the bare minimum. This class also has an unnecessarily difficult structure. The content isn't super hard, but there is SO MUCH of it, and you need to know it inside and out. You are not set up to succeed in this course. I would strongly recommend avoiding this class if you do not need it. The TAs try their best but a lot of them aren't helpful, unfortunately. No shade to them though, they try their best. Bloomfield does not provide you with resources to study for the final, you're all on your own. He gives past exams but no solution keys. You can try to figure it out with your friends. Given that so many other CS classes give past exams with answer keys I find his excuses against giving them completely meaningless. This class is finally getting some needed reform with the syllabus change. I don't know how they are structured but splitting up this class into 2 seems like a step forward. With all that being said, if you love CS and love it so much that you pour in hours and hours of your time into this class, you will do well. It's not impossible to do well. You're just not set up for it. #tCFfall2021", 5);
        Review review12 = new Review(frank, cs2150, "Aaron Bloomfield is incredibly lazy and uninvolved. His exams are made up of deceptive, poorly worded questions that test your understanding of grammar intricacies rather than content mastery. In his review sessions he very infrequently is able to answer his own questions from exams of past and is too lazy to attempt solving questions he deems are too long. Despite doing well in the course, I leave with a mediocre understanding of the material and an overall distaste for computer science. Unless you are majoring in cs DO NOT take this class, especially not with a professor this poor.", 5);
        Review review13 = new Review(greg, cs2150, "CS 2150 is definitely a CS weed out class, but teaches you a lot of great skill. The CS lab is essentially just a closed-off office hours session, but the assignment isn't due until midnight. The OH queue (starting from like Lab 4) will be 1-1.5 hour long. Make sure you go to OH, even if you think you don't need it. Also utilize Engineering tutoring!", 3);
        Review review14 = new Review(harry, cs2150, "A truly one-of-a-kind class, with Aaron Bloomfield who I firmly believe is the best teacher I have had at UVa. This class was what I needed to get to the next level as a programmer. By the end, I not only had a comprehensive knowledge of how to program in C++, but also could give the algorithm analysis and intuitions behind my code. Coding is something that can only be improved with deliberate practice. While comprehensive and sometimes difficult, the labs in this class gave me this practice and more. While it was a tough transition from other classes, it was such an amazing learning experience that I do not think I could have gotten any other way. Grade-wise, I think it is fair to say anyone who dedicates time, starts early enough on assignments, and has a genuine interest in Computer Science can expect to perform well. Taking this in a lighter semester will also allow you to fully appreciate the class which I would recommend also. Regardless, don't listen to rumors saying this class will ruin your life; if you start early you will be totally fine. A few final tips: go to Sunday office hours as the later you go the more packed they will be; ask the professors questions on things you don't understand, the class seems daunting at first, but Floryan and Bloomfield are so knowledgable they will help you instantly just talking to them; do as many past midterms before exams as you can, they're great prep; PSEUDO CODE - this is actually really important - I think one reason I really succeeded on the labs was I drafted out everything first on paper, and walked through it to make sure it made sense - then I started coding and it just flowed easier; code a little test a little - constantly test small parts of your code to make sure the whole thing works - will be way easier when mistakes come up; USE THE DEBUGGER - such an awesome practice you learn in this class that actually saves you loads of time. Again, what a great class. Enjoy the ride.", 1);
        Review review15 = new Review(ian, cs2150, "This was probably my favorite class that I've taken at UVA so far. There are tons of in-depth reviews already so I won't repeat them, but I will add a few comments. 1) This class was a bit less time consuming than previous years (I believe, I could be wrong though) because the going to your lab section was optional this semester - instead, lab sections were open office hours with TAs and the entire lab could just be done remotely. You have to go to your lab section for exams still, so make sure to pick a time that works with your schedule. 2) The labs that are commonly regarded as the hardest ones are 2,5,6, and 10. I would argue lab 2 actually isn't that bad (hear me out here). The primary reason lab 2 is challenging for most people is because it's most people's first time seeing pointers and working with c++. I spent maybe two or three hours learning c++ right before the semester started which allowed me to get ahead with the course early and as a result found lab 2 to be one of the easier labs, as linked lists are a very simple data structure. From personal experience, I highly recommend taking some time before the semester begins to get accustomed to basic c++ if you're worried about getting stuck early. Lab 5 was, in my opinion, the most challenging lab, so make sure to start on that one as early as you can. 3) Attend lecture! Professor Bloomfield is an incredible lecturer - easily the best and most dedicated lecturer I've encountered thus far at UVA.", 2);


        reviewsManager.addReview(review1);
        reviewsManager.addReview(review2);
        reviewsManager.addReview(review3);
        reviewsManager.addReview(review4);
        reviewsManager.addReview(review5);
        reviewsManager.addReview(review6);
        reviewsManager.addReview(review7);
        reviewsManager.addReview(review8);
        reviewsManager.addReview(review9);
        reviewsManager.addReview(review10);
        reviewsManager.addReview(review11);
        reviewsManager.addReview(review12);
        reviewsManager.addReview(review13);
        reviewsManager.addReview(review14);
        reviewsManager.addReview(review15);


        db.disconnect();

    }
}
