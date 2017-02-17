package simple_soccer_lib.utils;

public enum FieldFlags {
	//Esquerda
	l_b_30(-57.5,-30),
	l_b_20(-57.5,-20),
	l_b_10(-57.5,-10),
	l_0(-57.5,0),
	l_t_10(-57.5,10),
	l_t_20(-57.5,20),
	l_t_30(-57.5,30),
	//Baixo
	b_l_50(-50,-39),
	b_l_40(-40,-39),
	b_l_30(-30,-39),
	b_l_20(-20,-39),
	b_l_10(-10,-39),
	b_0(0,-39),
	b_r_10(10,-39),
	b_r_20(20,-39),
	b_r_30(30,-39),
	b_r_40(40,-39),
	b_r_50(50,-39),
	//Cima
	t_l_50(-50,39),
	t_l_40(-40,39),
	t_l_30(-30,39),
	t_l_20(-20,39),
	t_l_10(-10,39),
	t_0(0,39),
	t_r_10(10,39),
	t_r_20(20,39),
	t_r_30(30,39),
	t_r_40(40,39),
	t_r_50(50,39),
	//Direita
	r_b_30(57.5,-30),
	r_b_20(57.5,-20),
	r_b_10(57.5,-10),
	r_0(57.5,0),
	r_t_10(57.5,10),
	r_t_20(57.5,20),
	r_t_30(57.5,30),
	//Flags dos extremos internos
	l_b_0(-52.5,-34),
	l_t_0(-52.5,34),
	r_b_0(52.5,-34),
	r_t_0(52.5,34),
	c_b_0(0,-34),
	c_t_0(0,34),
	c_0(0,0);
	
	private double x;
	private double y;
	
	private FieldFlags(double x,double y){
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}	
}
