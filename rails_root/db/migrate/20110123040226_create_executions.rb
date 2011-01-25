class CreateExecutions < ActiveRecord::Migration
  def self.up
    create_table :executions do |t|
      t.integer :plan_id
      
      t.date :date

      t.timestamps
    end
  end

  def self.down
    drop_table :executions
  end
end
