package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

@SuppressWarnings({ "rawtypes", "unchecked"} ) 
public class ReportUtil implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	
/*-------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método para encontrar o sub_report, passando o nome do relatório*/
	public byte[] geraRelatorioExcel(List listaDados, String nomeRelatorio, HashMap<String, Object> params, ServletContext servletContext) throws Exception {
			
			/*Cria a lista de dados que vem do nosso SQL da consulta feita*/
			JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);
			
			String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";/*Caminho onde está o relatório, em qual pasta*/
			
			JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, params, jrbcds);
			
			JRExporter exporter = new JRXlsExporter(); /*Excel*/
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			
			exporter.exportReport();
			
			return baos.toByteArray();
			
		}
	
	
	/*-------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método para encontrar o sub_report, passando o nome do relatório*/
	public byte[] geraRelatorioPDF(List listaDados, String nomeRelatorio, HashMap<String, Object> params, ServletContext servletContext) throws Exception {
			
			/*Cria a lista de dados que vem do nosso SQL da consulta feita*/
			JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);
			
			String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";/*Caminho onde está o relatório, em qual pasta*/
			
			JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, params, jrbcds);
			
			return JasperExportManager.exportReportToPdf(impressoraJasper);
			
		}


	/*-------------------------------------------------------------------------------------------------------------------------*/

		/*Método que gera a lista de PDF*/
		public byte[] geraRelatorioPDF(List listaDados, String nomeRelatorio, ServletContext servletContext) throws Exception {
			
			/*Cria a lista de dados que vem do nosso SQL da consulta feita*/
			JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados);
			
			String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";/*Caminho onde está o relatório, em qual pasta*/
			
			JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashedMap(), jrbcds);
			
			return JasperExportManager.exportReportToPdf(impressoraJasper);
			
		}
	
	}
