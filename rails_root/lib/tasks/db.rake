namespace :db do
  Rake::Task['db:reset'].prerequisites.clear
  task :reset => %w(db:drop db:create db:migrate)

  Rake::Task['db:test:prepare'].prerequisites.clear
  namespace :test do
    task :prepare do
      Rails.env = 'test'
      Rake::Task['db:reset'].invoke
    end
  end  
  
  task :backup => :environment do
    cd File.join(Rails.root, 'db') do
      mkdir_p 'backup'
      cp "#{Rails.env}.sqlite3", "backup/#{Rails.env}.#{Time.now.to_f}.sqlite3"
    end
  end
  
  task :safe_migrate => ['db:backup', 'db:migrate']
end