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
(NULL,'teacher', 'teacher', 2);

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
PRIMARY KEY(ExamAssignmentID),
FOREIGN KEY(ExamID) REFERENCES ExamKeys(ExamKeyID),
FOREIGN KEY(AssignedStudentID) REFERENCES Users(UserID)
);

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

CREATE TABLE StudentExamSubmittedQuestions(
SubmittedQuestionID IDENTITY,
LinkedExamKeyID int,
SubmittedQuestionUser int,
SubmittedQuestionNumber int,
SubmittedAnswerChoice int,
PRIMARY KEY(SubmittedQuestionID),
FOREIGN KEY(SubmittedQuestionUser) REFERENCES Users(UserID),
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

INSERT INTO StudentExams(SubmitedExamID,StudentUserID,StudentExamID,ExamTaken)
VALUES(NULL,0,0,0),
(NULL,0,1,0),
(NULL,0,2,0);