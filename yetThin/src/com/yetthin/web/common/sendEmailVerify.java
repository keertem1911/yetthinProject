package com.yetthin.web.common;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class sendEmailVerify {

	@Resource(name="JavaMailSenderImpl")
	private static JavaMailSenderImpl sender;
	
	private static Executor excutor=Executors.newCachedThreadPool();
	
	private static String getFromAddress(){
		String fromName=sender.getUsername();
		return fromName;
	}
	
	public static void sendEmail(final String to,final String subject,final String text){
		
		if(to ==null&&text==null)
			return ;
		Runnable task=new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MimeMessage msg=sender.createMimeMessage();
				MimeMessageHelper helper=null;
				try {
					helper = new MimeMessageHelper(msg,true,"utf-8");
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					helper.setTo(to);
					helper.setSubject(subject);
					helper.setText(text);
					helper.setFrom(getFromAddress());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sender.send(msg);
			}
			
		};
		excutor.execute(task);
	}
	public static void main(String[] args) {
		 
	}
}
