class TodoItem
  attr_reader :habit_id, :title, :done
  
  def initialize(habit, done)
    @habit_id = habit.id
    @title = habit.title
    @done = done
  end
end