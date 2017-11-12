/* Comments */

CREATE TABLE Users(
UserID IDENTITY NOT NULL,
Username varchar(50),
Password varchar(50), 
AccountType int,
PRIMARY KEY(UserID)
);

INSERT INTO Users(UserID,Username,Password,AccountType)
VALUES(NULL,'USER', 'USER', 1),
(NULL,'TEACHER', 'TEACHER', 2);

CREATE TABLE TeacherExams(
TeacherExamID INT NOT NULL,
ExamID IDENTITY,
FOREIGN KEY(TeacherExamID) REFERENCES Users(UserID)
);

CREATE TABLE TeacherAssignments(
ExamAssignmentID IDENTITY NOT NULL,
ExamID int,
QuestionType int,
QuestionNumber int,
ExamQuestion varchar(500),
QuestionOne varchar(500),
QuestionTwo varchar(500),
QuestionThree varchar(500),
QuestionFour varchar(500),
PRIMARY KEY(ExamAssignmentID),
FOREIGN KEY(ExamID) REFERENCES TeacherExams(ExamID)
);

CREATE TABLE ExamKeys(
ExamKeyID IDENTITY,
ExamID int,
ExamTeacherID int,
ExamQuestionNumber int NOT NULL,
ExamCorrectAnswer int,
PRIMARY KEY(ExamKeyID),
FOREIGN KEY(ExamID) REFERENCES TeacherExams(ExamID)
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
FOREIGN KEY(StudentExamID) REFERENCES TeacherExams(ExamID)
);