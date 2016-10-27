package com.drc.test;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class XMLToObj {

	public static void main(String[] args) {
		System.out.println("******Process starts********");
		
		System.out.println("******Start unmarshalling********");
		Student student = unmarshall(); 
		System.out.println("******unmarshalling complete********");
		student.setGender("M");
		System.out.println("******Start marshalling********");
		marshall(student);
		System.out.println("******marshalling complete********");
		System.out.println("******Process complete********");
	}

	private static Student unmarshall() {
		Student student = new Student();
		try {

			//File file = new File("D:\\Abir\\student.xml");
			File file = new File("student.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			student = (Student) jaxbUnmarshaller.unmarshal(file);
			
			System.out.println(student);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return student;
	}


	private static void marshall(Student student) {
		try {
			//File file = new File("D:\\Abir\\updatedStudent.xml");
			File file = new File("updatedStudent.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(student, file);
			jaxbMarshaller.marshal(student, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
