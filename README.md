# SpringSandwich

## Update on progress 

Functionality implemented (most important things noted): 
* ordering sandwich (students and instructors that follow a session that day)
* ordering more than one sandwich 
* adding sandwich to sandwich shop 

NOT implemented:
* updating existing order 
* financial controller is not working yet 
* adding and removing persons (implemented in services but not via api yet)

## How to test 

1. Test via Postman 
* In the resource folder of our application, you can find zip files for the management and order controller with the corresponding URLs, so that you can just test and run
* Before ordering, always once run the "startup" url, so that orders are created for everyone (that will ideally be done by the admin at the beginning of a day). For now it selects a sandwich shop (Vleugels) and creates empty orders for everyone that day. 

2. Test via Unit tests 
* easy peasy, just run the tests in the application
