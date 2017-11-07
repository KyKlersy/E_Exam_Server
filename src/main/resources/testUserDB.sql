/* Comments */
DROP TABLE Users IF EXISTS;

CREATE TABLE Users(
UserID IDENTITY,
Username varchar(50),
Password varchar(50), 
AccountType int,
PRIMARY KEY(UserID)
);

INSERT INTO Users(UserID,Username,Password,AccountType)
VALUES(NULL,'USER', 'USER', 1);

CREATE TABLE TeacherExams(
TeacherExamID int NOT NULL,
ExamID IDENTITY,
PRIMARY KEY(TeacherExamID)
);

CREATE TABLE TeacherAssignments(
AssignmentID IDENTITY,
TeacherID int NOT NULL,
StudentID int NOT NULL,
ExamID int NOT NULL,
PRIMARY KEY (AssignmentID),
FOREIGN KEY (TeacherID) REFERENCES Users(UserID),
FOREIGN KEY (StudentID) REFERENCES Users(UserID),
FOREIGN KEY (ExamID) REFERENCES TeacherExams(ExamID)
);

CREATE TABLE ExamKeys(
ExamKeyID IDENTITY,
ExamID int,
ExamTeacherID int,
ExamQuestionNumber int,
ExamCorrectAnswer int,
PRIMARY KEY(ExamKeyID),
FOREIGN KEY(ExamID) REFERENCES TeacherExams(ExamID)
);

CREATE TABLE StudentExamSubmittedQuestions(
SubmittedQuestionID IDENTITY,
SubmittedQuestionUser int,
SubmittedQuestionNumber int,
SubmittedAnswerChoice int,
PRIMARY KEY(SubmittedQuestionID),
FOREIGN KEY(SubmittedQuestionUser) REFERENCES Users(UserID),
FOREIGN KEY(SubmittedQuestionNumber) REFERENCES ExamKeys(ExamQuestionNumber)
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