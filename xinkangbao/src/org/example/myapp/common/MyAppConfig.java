package org.example.myapp.common;

public class MyAppConfig {
	public static final int MOBILE_LEN = 11; //�ֻ��ų��ȣ�Ҳ����ID�ĳ���
	
	public static final long SUPER_ZIXUN_ACCOUNT = 11111111111L; //super ҽ���˻�

	public static final String SUPER_ZIXUN_NICK = "��Ҫ��ѯ";
	
	public static final String APP_VERSION = "1.0.0";
	
	
	//ע���û�
	public static final String USER_REGISTER_URL = "http://rj17701.sinaapp.com/index.php/patient/create";
	
	//�û���¼
	public static final String USER_LOGIN_URL = "http://rj17701.sinaapp.com/index.php/patient/login"; 
	                                            
	//��ȡ������Ϣ��Ϣ
	public static final String USER_INFO_URL = "http://rj17701.sinaapp.com/index.php/patient/one/"; 
	
	//�޸�����
	public static final String PASSWROD_CHANGE_URL = "http://1.rj17701.sinaapp.com/index.php/user/changepwd/"; 
	
	//�û����ҽ���Ĺ�ϵ
	public static final String USER_ADD_DOC_URL = "http://rj17701.sinaapp.com/index.php/doctorpatientrelationship/add/";
	
	//��ȡҽ������Ϣ
	public static final String DOC_INFO_URL = "http://rj17701.sinaapp.com/index.php/doctor/one/";
	
	//ɾ��ҽ���Ĺ���
	public static final String USER_DEL_DOC_URL = "http://rj17701.sinaapp.com/index.php/doctorpatientrelationship/delete/";
	
	//��ȡ��ǰ�û�ҽ�����б�
	public static final String USER_DOC_LIST_URL = "http://rj17701.sinaapp.com/index.php/doctorpatientrelationship/listing/";
		
	//��ȡ����ҽ�����б�
	public static final String DOC_ALL_URL = "http://rj17701.sinaapp.com/index.php/doctor/all/";
	
	//�����û�����Ϣ
	public static final String USER_UPDATE_INFO_URL = "http://rj17701.sinaapp.com/index.php/patient/update";
			
	//��ĳһ��ҽ��������Ϣ���Ȳ������߻�������
	public static final String USER_SEND_MSG_TO_DOC_URL = "http://rj17701.sinaapp.com/index.php/chatmessage/send";
	
	//��ȡ��ǰ�û���������Ϣ����Ҫ��δ���ģ��Ѿ���ȡ��һ�ε�������Ϣ�����Ϊ�Ѷ����´������ݿ⻹δ��ȡ����ϢΪδ��
	public static final String USER_MSG_TO_READ_URL = "http://rj17701.sinaapp.com/index.php/chatmessage/getunreadsummary/";
	
	//
	public static final String USER_DOC_CHAT_MSG_URL = "http://rj17701.sinaapp.com/index.php/chatmessage/getunreadsdetail/";

	public static final int UPDATE_MESSAGE_COUNT = 120000;
	
	
	public static final int GET_MESSAGE_COUNT = 15000;
	
	//�����洢
	public static final String SHARE_PREFERENCE_FILE = "three_stone_cradle";
	
	//ԤԼ���� post
	public static final String PATIENT_ADD_ORDER = "http://rj17701.sinaapp.com/index.php/appointmentrecord/addAppointment";
	
	//get
	public static final String PATIENT_DEL_ORDER = "http://rj17701.sinaapp.com/index.php/appointmentrecord/deleteAppointment/";
	
	//��ȡ�����б�
	public static final String PATIENT_GET_ORDER_LIST = "http://rj17701.sinaapp.com/index.php/appointmentrecord/GetAppointmentsByPat/";

	
	//��ȡ�����б�
	public static final String PATIENT_GET_POST_LIST = "http://rj17701.sinaapp.com/index.php/node/showInMobile/";

	//��������
	public static final String PATIENT_ADD_POST = "http://rj17701.sinaapp.com/index.php/topic/addFromMobile";

	//��ȡ������ϸ����
	public static final String PATIENT_GET_POST_DETAIL = "http://rj17701.sinaapp.com/index.php/topic/showTopicInMobile/";

	
	//��ȡ��������
	public static final String PATIENT_GET_POST_COMMENT_LIST = "http://rj17701.sinaapp.com/index.php/topic/showCommentsInMobile/";
	
	//������������
	public static final String PATIENT_ADD_POST_COMMENT = "http://rj17701.sinaapp.com/index.php/comment/addFromMobile";

	//��ȡ������Ϣ
	public static final String GET_PATIENT_HEALTH_STAT = "http://rj17701.sinaapp.com/index.php/healthyindex/statistics/";
	
	//��ȡ������Ϣ
	public static final String GET_PATIENT_HEARTRATE_LIST = "http://rj17701.sinaapp.com/index.php/healthyindex/recent/";

	//��ȡ�����豸
	public static final String GET_PATIENT_DEVICE = "http://rj17701.sinaapp.com/index.php/patient/getimeis/";

	//��ȡloc
	public static final String GET_PATIENT_LOC = "http://rj17701.sinaapp.com/index.php/location/last/";
	
	//��������
	public static final String GET_EDUCATION_ARTICLE = "http://1.rj17701.sinaapp.com/index.php/topic/showarticles";
	
	//��������
	public static final String GET_ARCHIVES = "http://1.rj17701.sinaapp.com/index.php/patient/patient_archives/";
	
	//��������
	public static final String GET_DISEASE_TYPE = "http://1.rj17701.sinaapp.com/index.php/sortword/getdiseaseword";
	
	//ҽԺ�б�
	public static final String GET_HOSPITAL_NAME = "http://1.rj17701.sinaapp.com/index.php/sortword/gethospitalword";
	
	//ѧ���б�
	public static final String GET_EDUCATION = "http://1.rj17701.sinaapp.com/index.php/sortword/getedu";
	
}
