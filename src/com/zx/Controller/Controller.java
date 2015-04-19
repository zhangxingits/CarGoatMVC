package com.zx.Controller;

import com.zx.Model.Model;
import com.zx.View.View;

public class Controller {
	
	public static void main(String[] args) {
		View view=new View();
		Model model = new Model();
		model.addView(view);
		view.update(model);

	}

}
