Embulk::JavaPlugin.register_filter(
  "gsub", "org.embulk.filter.gsub.GsubFilterPlugin",
  File.expand_path('../../../../classpath', __FILE__))
