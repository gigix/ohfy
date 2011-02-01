Ohfy::Application.routes.draw do
  root :controller => :calendars, :action => :show
  
  devise_for :users
  
  resource :calendar, :as => :calendar
  resources :plans
  
  resources :executions do
    resources :activities
  end
  
  resources :widgets
  match 'widgets-style/ohfy-widget.css', :controller => :widgets, :action => :css
  
  match "executions/:execution_id/habits/:habit_id", :controller => :activities, :action => :toggle
end
