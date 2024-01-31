create or replace trigger g_enrollments_trigger_insert
after insert on g_enrollments
for each row
begin
update classes set class_size=class_size+1 where classid= :new.classid;
end;
/

create or replace trigger g_enrollments_trigger_delete
after delete on g_enrollments
for each row
begin
update classes set class_size=class_size-1 where classid=:old.classid;
end;
/
 
create or replace trigger student_trigger_delete
after delete on students
for each row
begin
delete from g_enrollments where G_B#= :old.B#;
end;
/

create or replace trigger logs_delete_student
after delete on students
for each row
begin 
insert into logs values(log_id.nextval, user, sysdate, 'students', 'delete', :old.B#);
end;
/

create or replace trigger logs_insert_enrollments
after insert on g_enrollments
for each row
begin
insert into logs values(log_id.nextval, user, sysdate, 'g_enrollments', 'insert', :new.G_B# || ' ' || :new.classid);
end;
/

create or replace trigger logs_delete_enrollments
after delete on g_enrollments
for each row
begin
insert into logs values(log_id.nextval, user, sysdate, 'g_enrollments', 'delete', :old.G_B# || ' ' || :old.classid);
end;
/

show errors