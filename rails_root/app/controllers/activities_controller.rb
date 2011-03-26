class ActivitiesController < ApplicationController
  before_filter :authenticate_user!
  
  def toggle
    @execution = current_user.current_plan.executions.find(params[:execution_id])
    habit = @execution.habits.find(params[:habit_id])
    
    unless @execution.acted?(habit)
      @execution.act!(habit)
    else
      @execution.deact!(habit)
    end
    
    render :partial => 'executions/execution', :object => @execution, :layout => false
  end
end
