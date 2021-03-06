import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class ConvertMatcher {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ArrayList<String[]> scheduledTokenList = new ArrayList<String[]>();
		ArrayList<String[]> traceTokenList = new ArrayList<String[]>();
		final String refDumpDir = "C:\\Users\\Brandon\\Documents\\NMI\\ref\\";
		final String refChaseDir = "C:\\Users\\Brandon\\Documents\\NMI\\ref Chase\\";
		final String mainDir = "C:\\Users\\Brandon\\Documents\\NMI\\";
		final String comma = ",";
		final String quote = "\"";
		
		System.out.println("Chase Report to NMI");
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter Date: (MM/DD/YYYY) ");
		String date = scanner.next();
		String dateDir = date.replace("/", ""); 
		
		InputStream scheduled = new BufferedInputStream(new FileInputStream(refChaseDir + dateDir + "\\" + "Chase Report " + dateDir + ".csv"));
		InputStream trace = new BufferedInputStream(new FileInputStream(refDumpDir + "leadtrac dump.csv"));
		
		Reader scheduledReader = new InputStreamReader(scheduled);
		Reader traceReader = new InputStreamReader(trace);
		
		BufferedReader scheduledBuffer = new BufferedReader(scheduledReader);
		BufferedReader traceBuffer = new BufferedReader(traceReader);
		
		scheduledBuffer.readLine();
		String line;
		String[] scheduledTokens;
		while((line = scheduledBuffer.readLine()) != null)
		{
			scheduledTokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			scheduledTokenList.add(scheduledTokens);
		}
		System.out.print(scheduledTokenList.size());
		System.out.println(" drafts!");
		
		
		String lineTrace;
		String[] traceTokens;
		traceBuffer.readLine();
		while((lineTrace = traceBuffer.readLine()) != null)
		{
			traceTokens = lineTrace.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			traceTokenList.add(traceTokens);
		}
		System.out.print(traceTokenList.size());
		System.out.println(" leadtrac entries!");
		
		
		int matches = 0;
		int cvvnomatch = 0;
		
		final String outputDir = mainDir + "outputNMIDraft\\" + dateDir + "\\";
		new File(mainDir + "outputNMIDraft").mkdir();
		new File(mainDir + "outputNMIDraft\\" + dateDir).mkdir();
		
		File fileDraft = new File(outputDir + dateDir + " NMI draft upload.csv");
		if (!fileDraft.exists()) 
		{
			fileDraft.createNewFile();
		}
		
		FileWriter fwDraft = new FileWriter(fileDraft.getAbsoluteFile());
		BufferedWriter bwDraft = new BufferedWriter(fwDraft);
		bwDraft.write("\"cc_number\",\"cc_exp\",\"amount\",\"cvv\",\"first_name\",\"last_name\",\"address_1\",\"city\",\"state\",\"postal_code\",\"country\",\"order_id\",\"order_description\"");
		bwDraft.newLine();
		
		
		for(String[] ss : scheduledTokenList)
		{
			boolean match = false;
			for(String[] st: traceTokenList)
			{
				if(st[8].contains(ss[6]))
				{
					matches++;
					match = true;
					if(st[10].equals(""))
					{
						cvvnomatch++;
					}
					String[] dateTokens = date.split("/");
					//final String monthStart;
					//String yearStart = dateTokens[2];
					/*
					int month = Integer.parseInt(dateTokens[0]);
					month++;
					if(month < 10)
					{
						monthStart = "0" + Integer.toString(month);
					}
					else if(month == 12)
					{
						monthStart = "01";
						int year = Integer.parseInt(dateTokens[2]);
						year++;
						yearStart = Integer.toString(year);
					}
					else
					{
						monthStart = Integer.toString(month);
					}
					*/
					final String cc_number = ss[6];
					final String cc_exp = ss[7]/*.replace("/", "")*/;
					//final String amount = ss[5].replace("$", "");
					final double amtDouble = Double.parseDouble(ss[5]);
					DecimalFormat df = new DecimalFormat("0.00");
					final String amount = df.format(amtDouble/100);
					//final String amount = Double.toString(amtDouble/100);
					final String cvv = st[10];
					final String first_name = st[1];
					final String last_name = st[2];
					String address_1 = st[3];
					if(address_1.contains("\""))
					{
						address_1 = address_1.replace("\"", "");
					}
					final String city = st[4];
					final String state = st[5];
					final String postal_code = st[6];
					final String country = "USA";
					final String order_id = st[0];
					final String order_description = ss[10];
					//final String recurring = "add_subscription";
					//final String plan_payments = "0";
					//final String start_date = dateTokens[2] + dateTokens[0] + dateTokens[1];
					//final String month_frequency = "1";
					//final String day_of_month = dateTokens[1];

					
					bwDraft.write(
							quote + cc_number + quote + comma +
							quote + cc_exp + quote + comma +
							quote + amount + quote + comma +
							quote + cvv + quote + comma +
							quote + first_name + quote + comma +
							quote + last_name + quote + comma +
							quote + address_1 + quote + comma +
							quote + city + quote + comma +
							quote + state + quote + comma +
							quote + postal_code + quote + comma +
							quote + country + quote + comma +
							quote + order_id + quote + comma +
							quote + order_description + quote
							);
					bwDraft.newLine();
					
					/*
					bwRecur.write(
							quote + cc_number + quote + comma +
							quote + cc_exp + quote + comma +
							quote + cvv + quote + comma +
							quote + order_id + quote + comma +
							quote + order_description + quote + comma +
							quote + first_name + quote + comma +
							quote + last_name + quote + comma +
							quote + address_1 + quote + comma +
							quote + city + quote + comma +
							quote + state + quote + comma +
							quote + postal_code + quote + comma +
							quote + country + quote + comma +
							quote + recurring + quote + comma +
							quote + plan_payments + quote + comma +
							quote + amount + quote + comma +
							quote + start_date + quote + comma +
							quote + month_frequency + quote + comma +
							quote + day_of_month + quote
							);
					bwRecur.newLine();
					*/
					/*
					StringBuilder content = null;
					content.append(ss[3] + ",");
					content.append(st[9]);
					content.append(",");
					content.append(ss[5]);
					content.append(",");
					content.append(st[10]);
					content.append(",");
					content.append(st[1]);
					content.append
					*/
					
					
					
					
					//File fileDraft = File;
					/*
					File file = new File("/users/mkyong/filename.txt");
					if (!file.exists()) 
					{
						file.createNewFile();
					}
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(content);
					bw.close();
		 
					System.out.println("Done!");
					*/
					break;
				}
			}
			if(!match)
			{
				//add unmatched into output
				System.out.print("Cannot find ");
				System.out.println(ss[3] + ". Please update CC, EXP, and CVV on leadtrac dump");
			}
		}
		
		
		
		
		scheduledBuffer.close();
		traceBuffer.close();
		bwDraft.close();
		
		System.out.print(matches);
		System.out.println(" matches found!");
		
		System.out.print(cvvnomatch);
		System.out.println(" missing cvv!");
		
		System.out.println("Done");
	}

}
