namespace :db do
  Rake::Task['db:reset'].prerequisites.clear
  task :reset => %w(db:drop db:create db:migrate)

  Rake::Task['db:test:prepare'].prerequisites.clear
  namespace :test do
    task :prepare do
      RAILS_ENV = 'test'
      Rake::Task['db:reset'].invoke
    end
  end  
end