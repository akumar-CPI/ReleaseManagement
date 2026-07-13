import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

def Message processData(Message message) {

    def body = message.getBody(String)

    def employees = new XmlSlurper().parseText(body)

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.DepartmentEmployees {

        employees.Employee.groupBy { it.DeptCode.text() }.each { dept, empList ->

            Department(name: dept) {

                empList.each { emp ->

                    Employee {
                        EmpNumber(emp.EmpNumber.text())
                        FullName(emp.FullName.text())
                        ContactEmail(emp.ContactEmail.text())
                        AnnualSalary(emp.AnnualSalary.text())
                        Status(emp.Status.text())
                    }
                }
            }
        }
    }
    message.setProperty("TestProperty","Changes")

    message.setBody(writer.toString())
    return message
}