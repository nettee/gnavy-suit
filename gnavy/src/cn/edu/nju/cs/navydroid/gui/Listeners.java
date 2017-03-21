package cn.edu.nju.cs.navydroid.gui;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class Listeners {
	
	public static interface DefaultSelectionListener extends SelectionListener {

		@Override
		default void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}
		
	}
	
	public static interface DefaultFocusListener extends FocusListener {
		
		@Override
		default void focusLost(FocusEvent e) {
			// do nothing
		}
	}
	
	public static interface FocusGainedListener extends FocusListener {
		
		@Override
		default void focusLost(FocusEvent e) {
			throw new IllegalStateException();
		}
	}
	
	public static interface FocusLostListener extends FocusListener {
		
		@Override
		default void focusGained(FocusEvent e) {
			throw new IllegalStateException();
		}
	}
	
	public static interface DoubleClickMouseListener extends MouseListener {

		@Override
		default void mouseUp(MouseEvent e) {
			// do nothing
		}

		@Override
		default void mouseDown(MouseEvent e) {
			// do nothing
		}
		
	}
	
	public static SelectionListener selection(DefaultSelectionListener listener) {
		return listener;
	}
	
	public static FocusListener gainFocus(DefaultFocusListener listener) {
		return listener;
	}
	
	public static FocusListener focus(FocusGainedListener focusGainedListener, FocusLostListener focusLostListener) {
		return new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				focusLostListener.focusLost(e);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				focusGainedListener.focusGained(e);
			}
		};
	}
	
	public static MouseListener mouseDoubleClick(DoubleClickMouseListener listener) {
		return listener;
	}

}
