package test.sap.fpa.security;

import org.openqa.selenium.By;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.repeat.Repeat;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class TeamsHelper {
	/**
	 * go to an expected team
	 * @param bw
	 * @param team
	 */
	private static void switchTeam(BrowserWindow bw, String team) {
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "repeat");
		rpt.clickRow(team);
	}

	
	public static void addTeam(BrowserWindow bws, String team,String teamdesc,String...users) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "createTeam-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByClass(bws, "sapMDialog");
		((Input) dlg.getUI5ObjectByIdEndsWith("teamName")).setValue(team,false);
		((Input) dlg.getUI5ObjectByIdEndsWith("teamDesc")).setValue(teamdesc,false);
		((Button)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "addTeamMember")).click();
		MemberSelector ms = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "teamMember-selector");
		ms.multiSearch("ID", users);
		ms.ok();
		dlg.ok();
		
	}
	
	public static boolean deleteTeams(BrowserWindow bws, String...teams){
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bws, "repeat");
		if(!rpt.selectRows(teams)){
			return false;
		}
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "delTeam-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "confirmRemoveTeamDlg");
		SapUi5Utilities.waitUntilMessageDisappear(bws.driver());
		dlg.ok();
		return true;
	}
	public static void assertTeamMembers(BrowserWindow bws,String team,String...users){
		switchTeam(bws,team);
		Dialog dlg = SapUi5Factory.findUI5ObjectByClass(bws, "sapMDialog");
		for(String user:users){
			dlg.getWebElement(By.xpath(".//label[contains(@class,'teamList-members-name') and text()='"+user+"']"));
		}
		dlg.ok();
	}
	
	public static void updateTeam(BrowserWindow bws,String team, String desc,String...users){
		switchTeam(bws,team);
		Dialog dlg = SapUi5Factory.findUI5ObjectByClass(bws, "sapMDialog");
		if(desc!=null){
			((Input) dlg.getUI5ObjectByIdEndsWith("teamDesc")).setValue(desc,false);
		}
		
		for(String user:users){
			try{
				dlg.getWebElement(By.xpath(".//label[text()='"+user+"']"));
			}catch(Exception e){
				
			}
			
		}
		dlg.ok();
	}
	
	public static void refreshTeams(BrowserWindow bws){
		NewToolbarItem refresh = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "refreshTeam-btn");
		refresh.click();
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bws, "repeat");
		rpt.watiUntilRefreshed();
	}
	
}
