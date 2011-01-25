class CreateHabits < ActiveRecord::Migration
  def self.up
    create_table :habits do |t|
      t.integer :plan_id
      
      t.string :title

      t.timestamps
    end
  end

  def self.down
    drop_table :habits
  end
end
