package simple_soccer_lib.perception;

import simple_soccer_lib.comm.FlagInfo;
import simple_soccer_lib.comm.LineInfo;
import simple_soccer_lib.comm.ObjectInfo;
import simple_soccer_lib.comm.PlayerInfo;
import simple_soccer_lib.comm.VisualInfo;
import simple_soccer_lib.utils.FieldFlags;
import simple_soccer_lib.utils.FieldDimensions;
import simple_soccer_lib.utils.Vector2D;

public class ParsePerception {
	
	/** Faz o parser populando todas as percepcoes **/
	public static void parse(PlayerPerception player, FieldPerception field, VisualInfo info){
		setCurrentPlayerPosition(player, info);
		setCurrentPlayerHeadDirection(player,info);
		setPlayersPositions(player,field, info);
		setMatchTime(field, info);
		setBallPosition(player, field, info);
	}
	
	/** Seta direcao da visao do player **/
	private static void setCurrentPlayerHeadDirection(PlayerPerception player, VisualInfo info){
		double angle = 0;
		for(ObjectInfo object : info.getLineList()){
			LineInfo li = (LineInfo) object;
			if(li.m_kind=='t'){
				if(li.m_direction>0) angle = 0+li.m_direction;
				else angle = 180+li.m_direction;
			}
			else if(li.m_kind=='b'){
				if(li.m_direction>0) angle = 180+li.m_direction;
				else angle = 360+li.m_direction;
			}
			else if(li.m_kind=='r'){
				if(li.m_direction>0) angle = 270+li.m_direction;
				else angle = 90+li.m_direction;
			}
			else if(li.m_kind=='l'){
				if(li.m_direction>0) angle = 90+li.m_direction;
				else angle = 270+li.m_direction;
			}
			break;
		}
		angle = Math.PI*angle/180;
		player.setHeadDirection(new Vector2D(Math.cos(angle),Math.sin(angle)));
	}
	
	private static FieldFlags getFlag(FlagInfo fi){
		String name = "";
		if(fi.m_type!=' ') name += fi.m_type;
		
		if(fi.m_pos1!=' ' && !name.equals("")) name += "_"+fi.m_pos1;
		else name += fi.m_pos1;
			
		if(fi.m_pos2!=' ') name += "_"+fi.m_pos2;
		if(fi.m_num!=' ') name += "_"+fi.m_num;
		
		FieldFlags flag = FieldFlags.valueOf(name);
		return flag;
	}
	
	/** Seta posicao do player em relacao ao campo **/
	private static void setCurrentPlayerPosition(PlayerPerception player, VisualInfo info){
		int i=0;
		Vector2D v1=null,v2=null, result=null;
		double d1=0.0,d2=0.0;
		for(ObjectInfo object : info.getFlagList()){
			FlagInfo fi = (FlagInfo) object;
			if(fi.m_type==' ' && i==0 && !fi.empty){
				d1 = fi.m_distance;
				FieldFlags flag = getFlag(fi);
				if(flag==FieldFlags.c_0) continue;
				v1 = new Vector2D(flag.getX(),flag.getY());
				i++;
			}
			else if(fi.m_type==' ' && i==1 && !fi.empty){
				d2 = fi.m_distance;
				FieldFlags flag = getFlag(fi);
				if(flag==FieldFlags.c_0) continue;
				v2 = new Vector2D(flag.getX(),flag.getY());
				i++;
			}
			if(i>1) {
				if(v1==null || v2==null) System.out.println("error");
				else {
					Vector2D[] vectors = Vector2D.intersections(v1, v2, d1, d2);
					if(vectors==null) {
						i=1;
						continue;
					}
					else {
						if(vectors[0].getX()>FieldDimensions.LIMIT_HORIZONTAL || vectors[0].getX()<FieldDimensions.LIMIT_HORIZONTAL*-1 ||
							vectors[0].getY()>FieldDimensions.LIMIT_VERTICAL || vectors[0].getY()<FieldDimensions.LIMIT_VERTICAL*-1){
							result = vectors[1];
						}
						else if(vectors[1].getX()>FieldDimensions.LIMIT_HORIZONTAL || vectors[1].getX()<FieldDimensions.LIMIT_HORIZONTAL*-1 ||
								vectors[1].getY()>FieldDimensions.LIMIT_VERTICAL || vectors[1].getY()<FieldDimensions.LIMIT_VERTICAL*-1){
							result = vectors[0];
						}
						else i=1;	
					}
				}
			}
		}
		
		if(result != null){
			player.setPosition(result);	
		}
	}
	
	/** Seta todos os players visiveis pelo agente **/
	private static void setPlayersPositions(PlayerPerception player, FieldPerception field, VisualInfo info){
		field.getAllPlayers().clear();
		for(PlayerInfo playerInfo : info.getPlayerList()){
			PlayerPerception p = new PlayerPerception(playerInfo);
			Vector2D otherPlay = player.getHeadDirection().scale(playerInfo.m_distance).sum(player.getPosition());
			p.setPosition(otherPlay);
			field.getAllPlayers().add(p);
		}
	}
	
	/** Seta tempo da partida **/
	private static void setMatchTime(FieldPerception field, VisualInfo info){
		field.setTime(info.getTime());
	}
	
	/** Seta posicao da bola **/
	private static void setBallPosition(PlayerPerception player, FieldPerception field, VisualInfo info){
		if(info.getBall()!=null){
			Vector2D ball = player.getHeadDirection().scale(info.getBall().m_distance).sum(player.getPosition());
			field.setBall(new ObjectPerception(ball));
		}
	}
}
