package com.masahiro.nakamoto.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.masahiro.nakamoto.domain.Invoice;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * ドライバー個人ページから請求書を作成するサービス
 */
@Service
public class InvoiceService {

	@Autowired
    ResourceLoader resource;

	/**
	 * Jasperテンプレートにデータを渡す
	 *
	 * @param params
	 * @param data
	 * @return
	 */
	public byte[] OrderReporting2(Map<String, Object> params, List<Invoice> data) {
        InputStream input;
        try {
            //帳票ファイルを取得
            input = new FileInputStream(resource.getResource("classpath:report/invoice.jrxml").getFile());
            //リストをフィールドのデータソースに
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            //帳票をコンパイル
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            JasperPrint jasperPrint;
            //パラメーターとフィールドデータを注入
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            //帳票をByte形式で出力
            return  JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (JRException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

	/**
	 * 電話番号をハイフン表記に変更
	 *
	 * @param tel
	 * @return
	 */
	public String makePhoneNumber(String tel) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		String a = "";
		try {
			PhoneNumber phoneNumber = phoneUtil.parse(tel, "JP");
			a = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
		} catch (NumberParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return a;
	}

}
