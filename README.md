# aigou-220
220爱购商城后台

#启动FASTDFS命令
service fdfs_trackerd start
service fdfs_storaged start
/usr/bin/fdfs_monitor /etc/fdfs/storage.conf  监视器
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf  提供web服务
测试访问关闭防火墙
service iptables stop/status
