module PlansHelper
  def delete_button(plan)
		if plan.removable?
			button_to "Delete Plan", {:controller => :plans, :action => :destroy, :id => plan.id}, :confirm => "Are you sure?", :method => :delete
		end
  end
end
