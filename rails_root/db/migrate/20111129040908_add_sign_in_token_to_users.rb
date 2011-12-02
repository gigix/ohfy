class AddSignInTokenToUsers < ActiveRecord::Migration
  def self.up
    add_column :users, :sign_in_token, :string
  end

  def self.down
    remove_column :users, :sign_in_token
  end
end
