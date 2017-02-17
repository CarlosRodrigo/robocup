package keyboard_team;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

/**
 * Ações retornadas pelo controlador.
 */
enum ActionPressed {
	RUN,
	TURN_LEFT,
	TURN_RIGHT,
	TURN_BACK,
	KICK
}

/**
 * Esta classe lê teclas específicas do teclado e retorna a ação correspondente
 * quando uma tecla é pressionado.   
 * 
 * @author Pablo Sampaio
 */
class KeyboardController implements KeyEventDispatcher {
    private KeyActionInfo[] keys;
    private int nextKey; //para implementar um revezamento -- uma como "getAction()" retorna apenas uma ação, esse revezamento evita 
                         //que retorne sempre a mesma, caso mais de uma tecla seja pressionada
    
    
    public KeyboardController() {
    	this(KeyEvent.VK_W, KeyEvent.VK_A, 
    			KeyEvent.VK_D, KeyEvent.VK_S, 
    			KeyEvent.VK_B);
    }
    
    public KeyboardController(int codeGo, int codeLeft, int codeRight, int codeBack, int codeKick) {
    	keys = new KeyActionInfo[5];
    	keys[0] = new KeyActionInfo(codeGo   , false, ActionPressed.RUN);
    	keys[1] = new KeyActionInfo(codeLeft , false, ActionPressed.TURN_LEFT);
    	keys[2] = new KeyActionInfo(codeRight, false, ActionPressed.TURN_RIGHT);
    	keys[3] = new KeyActionInfo(codeBack , true , ActionPressed.TURN_BACK);
    	keys[4] = new KeyActionInfo(codeKick , true , ActionPressed.KICK);

    	nextKey = 0;
    	KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }
    
	@Override
	public boolean dispatchKeyEvent(KeyEvent ke) {
		//System.out.println(ke);
        for (int i = 0; i < keys.length; i++) {
        	if (ke.getKeyCode() == keys[i].keyCode) {
				if (ke.getID() == KeyEvent.KEY_PRESSED) {
					synchronized (this) {
						keys[i].pressed = true;
					}
				} else if (ke.getID() == KeyEvent.KEY_RELEASED) {
					synchronized (this) {
						keys[i].pressed = false;
					}
				}
				return true;
        	}
		}
        return false;
    }
   
    public synchronized ActionPressed getAction() {
        ActionPressed action = null;
        //testa todas as teclas, partindo da próxima (nextKey)
	    for (int i = 0; i < keys.length; i++) {
		    if (keys[nextKey].pressed) {
			    action = keys[nextKey].action;
			    if (keys[nextKey].fireOnce) {
			    	keys[nextKey].pressed = false;
			    }
			    nextKey = (nextKey + 1) % keys.length;
			    break;
		    }
		    nextKey = (nextKey + 1) % keys.length;
	    }
        return action;
    }
    
    class KeyActionInfo {
    	private final int keyCode;
    	private final boolean fireOnce; //quando é verdade, pressionar por muito tempo, só dispara uma ação
    	private final ActionPressed action;
    	
    	private boolean pressed;
    	
    	KeyActionInfo(int k, boolean single, ActionPressed action) {
    		this.keyCode = k;
    		this.fireOnce = single;
    		this.action = action;
    	}
    }

    public static void main(String[] args) {
    	KeyboardController keyb = 
    		//new KeyboardController(); //*/
    		new KeyboardController(KeyEvent.VK_UP, KeyEvent.VK_LEFT,
    								KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN,
    								KeyEvent.VK_NUMPAD0); //*/

    	JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(640, 480);

		ActionPressed action; 
    	while (true) {
    		action = keyb.getAction();
			if (action != null) {
				System.out.println("Action = " + action);
			}
		}
	}
    
}