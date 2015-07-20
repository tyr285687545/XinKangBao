package org.example.myapp.client.model;

import java.io.Serializable;

public class ArchivesBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fzjc;// 表示基本辅助检查；
	private String sick;// 出院诊断；
	private String medicine;// 治疗用药；
	private String patient;// 基本信息；
	private String name;// 姓名
	private String sex;// 性别
	private String age;// 年龄
	private String tel;// 联系电话
	private String address;// 家庭住址
	private String life;// 独居
	private String customhospital;// 就诊医院
	private String personaldoctor;// 就诊医生
	private String department;// 就诊科室
	private String time;// 就诊时间
	private String guardian;// 监护人
	private String guardian_tel;// 监护人电话
	
	public String getFzjc() {
		return fzjc;
	}

	public void setFzjc(String fzjc) {
		this.fzjc = fzjc;
	}

	public String getSick() {
		return sick;
	}

	public void setSick(String sick) {
		this.sick = sick;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLife() {
		return life;
	}

	public void setLife(String life) {
		this.life = life;
	}

	public String getCustomhospital() {
		return customhospital;
	}

	public void setCustomhospital(String customhospital) {
		this.customhospital = customhospital;
	}

	public String getPersonaldoctor() {
		return personaldoctor;
	}

	public void setPersonaldoctor(String personaldoctor) {
		this.personaldoctor = personaldoctor;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGuardian() {
		return guardian;
	}

	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}

	public String getGuardian_tel() {
		return guardian_tel;
	}

	public void setGuardian_tel(String guardian_tel) {
		this.guardian_tel = guardian_tel;
	}
}
