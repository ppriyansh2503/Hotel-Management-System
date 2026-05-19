Create database hotelmanagementsystem;
show databases;
use hotelmanagementsystem;

create table login(username varchar(25), password varchar(25));
insert into login values('admin', '12345');
insert into login values('Youraj', '54321');
select * from login;

create table employee(name varchar(25), age varchar(10), gender varchar(15), job varchar(30), salary varchar(15), phone varchar(15), email varchar(40), aadhar varchar(20));
describe employee;
select * from employee;

create table room(roomNo varchar(20), available varchar(20), status varchar(25), price varchar(30), type varchar(25));
select * from room;

create table driver(name varchar(30), age varchar(20), gender varchar(20), company varchar(40), brand varchar(40), available varchar(20), location varchar(40));
select * from driver;

create table customer(document varchar(20), number varchar(30), name varchar(30), gender varchar(15), country varchar(30), room varchar(20), checkintime varchar(80), deposit varchar(25)); 
select * from customer;

create table department(department varchar(30), budget varchar(30));
insert into department values('Front Office', '500000');
insert into department values('Housekepping', '80000');
insert into department values('Food and Beverage', '100000');
insert into department values('Accounts', '450000');
insert into department values('Security', '700000');
select * from department;