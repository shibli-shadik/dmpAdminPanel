package dmp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDate.Property;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UtilServices {
    
    public LocalDate convertStringToDate(String dateInString,
            DateTimeFormatter format) {
        LocalDate lDate = org.joda.time.LocalDate.parse(dateInString, format);
        
        return lDate;
    }
    
    public Timestamp getTodaysDate() {
        java.util.Date today = new java.util.Date();
        
        return new java.sql.Timestamp(today.getTime());
    }
    
    public boolean isEmpty(String strToCmp) {
        
        return strToCmp == null || strToCmp.isEmpty();
    }
    
    public String convertDateToString(LocalDate givenDate,
            DateTimeFormatter formatPattern) {
        
        String formattedDate = givenDate.toString(formatPattern);
        
        return formattedDate;
    }
    
    public String convertDateToString(LocalDateTime givenDateTime,
            DateTimeFormatter formatPattern) {
        
        String formattedDate = givenDateTime.toString(formatPattern);
        
        return formattedDate;
    }
    
    public int daysOfMonth(int year, int month) {
        DateTime dateTime = new DateTime(year, month, 14, 12, 0, 0, 000);
        return dateTime.dayOfMonth().getMaximumValue();
    }
    
    public String getNameOfDayFromDate(LocalDate localDate) {
        Property pDoW = localDate.dayOfWeek();
        String nameOfDay = pDoW.getAsText(Locale.getDefault());
        
        return nameOfDay;
    }
    
    public String getNameOfMonthFromDate(LocalDate localDate) {
        Property pDoW = localDate.dayOfMonth();
        String nameOfMonth = pDoW.getAsText(Locale.getDefault());
        
        return nameOfMonth;
    }
    
    public String getMonthName(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
    
    public String getShortMonthName(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }
    
    public Blob getBlobData(String filePath) throws IOException,
            SQLException {
        byte[] bytes = Files.readAllBytes(new File(filePath).toPath());
        return new SerialBlob(bytes);
    }
    
    public Blob getBlobData(MultipartFile file) throws IOException,
            SQLException {
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }
    
    public Blob getBlobData(byte[] bytes) throws IOException,
            SQLException {
        return new SerialBlob(bytes);
    }
    
    public java.sql.Timestamp convertStringToTimestamp(String strDate, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format);
        Date date = (Date)formatter.parse(strDate);
        java.sql.Timestamp tsDate = new java.sql.Timestamp(date.getTime());
        
        return tsDate;
    }
    
    public Date convertStringToDate(String strDate, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format);
        Date date = (Date)formatter.parse(strDate);
        
        return date;
    }
    
    public String convertTimestampToDate(Object objTimestamp,
            String format) {
        
        java.sql.Timestamp timestamp = (java.sql.Timestamp) objTimestamp;
        DateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date(timestamp.getTime());
        
        return formatter.format(date);
    }
    
    public Date convertTimestampToDate(String strDate,
            String strFormat)
    {
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException ex) {
            Logger.getLogger(UtilServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    public String getFullTime()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        int miliSecond = Calendar.getInstance().get(Calendar.MILLISECOND);
        
        return String.valueOf(year)+String.valueOf(month)+String.valueOf(day)+String.valueOf(hour)
                +String.valueOf(minute)+String.valueOf(second)+String.valueOf(miliSecond);
    }
    
    public String getMaskedPan(String panOrAccountNo)
    {
        int length = panOrAccountNo.length();
        String preFix = panOrAccountNo.substring(0, 6);
        String postFix = panOrAccountNo.substring((length - 4), length);
        
        StringBuilder maskChar = new StringBuilder();
        for(int i = 0; i < (length - 10); i++)
        {
            maskChar.append("X");
        }
        
        return panOrAccountNo = preFix + maskChar + postFix;
    }
    
    public String getRandomString(int length)
    {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        
        String generatedString = buffer.toString().toUpperCase();
        
        return generatedString;
    }
    
    public String getSalt(int size) {
        
        final SecureRandom random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        
        String encodedSalt = Base64.encodeBase64String(salt);
        
        return encodedSalt;
    }
    
    public String getCurrentDate()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        return String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
    }
}
