/* Comments */

CREATE TABLE Users(
UserID IDENTITY NOT NULL,
Username varchar(50),
Password varchar(50), 
AccountType int,
PRIMARY KEY(UserID)
);

INSERT INTO Users(UserID,Username,Password,AccountType)
VALUES(NULL,'user', 'user', 1),
(NULL,'teacher', 'teacher', 2),
(NULL,'tom', 'tom', 1),
(NULL,'brady', 'tom', 1),
(NULL,'jim', 't', 1),
(NULL,'bob', 't', 1);

CREATE TABLE ExamKeys(
ExamKeyID IDENTITY,
ExamTeacherID INT NOT NULL,
ExamName varchar(50),
PRIMARY KEY(ExamKeyID),
FOREIGN KEY(ExamTeacherID) REFERENCES Users(UserID)
);

INSERT INTO ExamKeys(ExamKeyID, ExamTeacherID, ExamName)
VALUES(NULL,1,'Exam 1'),
(NULL,1,'Exam 2'),
(NULL,1,'Exam 3');

CREATE TABLE TeacherExams(
ExamNumber IDENTITY,
TeacherID INT NOT NULL,
ExamID INT NOT NULL,
FOREIGN KEY(TeacherID) REFERENCES Users(UserID),
FOREIGN KEY(ExamID) REFERENCES ExamKeys(ExamKeyID),
PRIMARY KEY(ExamNumber)
);

INSERT INTO TeacherExams(ExamNumber,TeacherID,ExamID)
VALUES(NULL,1, 0),
(NULL, 1, 1),
(NULL, 1, 2);

CREATE TABLE TeacherAssignments(
ExamAssignmentID IDENTITY,
ExamID INT NOT NULL,
AssignedStudentID INT NOT NULL,
TeacherAssignerID INT NOT NULL,
PRIMARY KEY(ExamAssignmentID),
FOREIGN KEY(ExamID) REFERENCES ExamKeys(ExamKeyID),
FOREIGN KEY(AssignedStudentID) REFERENCES Users(UserID),
FOREIGN KEY(TeacherAssignerID) REFERENCES Users(UserID)
);

INSERT INTO TeacherAssignments(ExamAssignmentID,ExamID,AssignedStudentID,TeacherAssignerID)
VALUES(NULL,0,0,1),
(NULL,1,0,1),
(NULL,2,0,1);

CREATE TABLE ExamQuestions(
ExamQuestionID IDENTITY NOT NULL,
ExamID INT NOT NULL,
QuestionType int,
QuestionNumber int,
ExamCorrectAnswer int,
ExamQuestion varchar(500),
QuestionOne varchar(500),
QuestionTwo varchar(500),
QuestionThree varchar(500),
QuestionFour varchar(500),
PRIMARY KEY(ExamQuestionID),
FOREIGN KEY(ExamID) REFERENCES ExamKeys(ExamKeyID)
);

INSERT INTO EXAMQUESTIONS(ExamQuestionID,ExamID,QuestionType,QuestionNumber,ExamQuestion,QuestionOne,QuestionTwo,QuestionThree,QuestionFour)
VALUES(NULL,1,1,1,'Is a Duck a Horse','','','',''),
(NULL,1,2,2,'Is a Tomato a fruit?', 'Yes', 'No', 'Maybe', 'Don''t Know');


CREATE TABLE StudentExamSubmittedQuestions(
SubmittedQuestionID IDENTITY,
LinkedExamKeyID int,
SubmittedQuestionUser int,
SubmittedQuestionNumber int,
SubmittedAnswerChoice int,
PRIMARY KEY(SubmittedQuestionID),
FOREIGN KEY(SubmittedQuestionUser) REFERENCES Users(UserID),
FOREIGN KEY(SubmittedQuestionNumber) REFERENCES ExamQuestions(ExamQuestionID),
FOREIGN KEY(LinkedExamKeyID) REFERENCES ExamKeys(ExamKeyID)
);

CREATE TABLE StudentExams(
SubmitedExamID IDENTITY,
StudentUserID int,
StudentExamID int,
ExamTaken bit,
Grade float,
PRIMARY KEY(SubmitedExamID),
FOREIGN KEY(StudentUserID) REFERENCES Users(UserID),
FOREIGN KEY(StudentExamID) REFERENCES ExamKeys(ExamKeyID)
);

INSERT INTO StudentExams(SubmitedExamID,StudentUserID,StudentExamID,ExamTaken,Grade)
VALUES(NULL,0,0,1,NULL),
(NULL,0,1,0, .85),
(NULL,0,2,0,NULL);