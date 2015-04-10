#Dexcoder���ٿ������߰���Ŀǰ��Ҫ�����¹��������

- ����Spring JdbcTemplate��ͨ��dao
- ��Apache BeanUtils��ǿ���Ч��Beanת��������

##����Spring JdbcTemplate��ͨ��daoʹ��˵��

����㲻ϲ����`Hibernate`��`Mybaits`����ORM��ܣ�ϲ��`JdbcTemplate`��`DbUtils`����ô�������������װ��ͨ��dao���������Ŀǰ��װ������ͨ��dao�ˡ�

��ͨ��dao����ʹ�ù����У���Գ���ķ���dao����������һЩ������������˸Ľ�����������ѭ��Լ���������õ�ԭ�򣬵���Լ�����£�

- ����Լ�� `USER_INFO`��ʵ������Ϊ`UserInfo`��
- �ֶ���Լ�� `USER_NAME`ʵ������������Ϊ`userName`��
- ������Լ�� `USER_INFO`��������Ϊ`USER_INFO_ID`��ͬ��ʵ������������Ϊ`userInfoId`��
- `Oracle`������Լ�� `USER_INFO`���Ӧ������������Ϊ`SEQ_USER_INFO`

��Ȼ����Щ���������չ�иı���������������ô�����Ȿ�����һ�����õĹ淶��

Ŀǰ���˾��г��淺��dao�Ĺ����⻹�����������ԣ�

1. һ��dao���Բ������е�ʵ���࣬������һ����������ʵ���Ӧ�ļ̳�������BaseDao���ࡰͨ��dao���ˡ�
2. ���෽������֧��`Entity`��`Criteria`���ַ�ʽ��
3. sql��where����֧�ֲ�����`(column != value)`�����ڶ��ֵ`(column = value1 or column = value2)`��in�ķ�ʽ`(column in (value1,value2,...))`��
4. �����ڲ�ѯʱָ��ʹ���ĸ��ֶν������򣬿���ָ��������������������������
5. ֧���ڲ�ѯʱָ�������ֶεİ������ͺ�����������ָ��ֻ����ĳЩ�ֶλ򲻷���ĳ�����ֶΡ�
6. ����ǿ��ķ�ҳ���ܣ������������������д���㶨��ҳ���Զ��ж����ݿ⣬����ָ����

Ҫ����Ŀ��ʹ��ͨ��daoʮ�ּ򵥣�ֻ��Ҫ��spring�������ļ�����������bean��
    <pre>
	`<bean id="jdbcDao" class="com.dexcoder.assistant.persistence.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
    <!--��Ҫ��ҳʱ����-->
    <bean id="pageControl" class="com.dexcoder.assistant.interceptor.PageControl"/>`
    </pre>
�������Ϳ���ע�뵽����`Service`������������ʹ���ˡ�

##������һЩ���õķ���ʾ���������`Entity`����Ϊ`User`�������κε�`Entity`����һ����.

������һ��`User`�������̳е�`Pageable`

	public class User extends Pageable {
		private Long    userId;
		private String  userName;
		private Integer userAge;
		private Date    gmtCreate;
		private Date    gmtModify;
		//......
	}
	
Pageable������������ҳ�롢ÿҳ������Ϣ��֧�ַ�ҳ

	public class Pageable implements Serializable {
		/** ÿҳ��ʾ���� */
		protected int             itemsPerPage     = 20;
		/** ��ǰҳ�� */
		protected int             curPage          = 1;
		
		//......
	}
	
������ͨ��JavaBean����������������ν��о������ɾ�Ĳ飬ÿ�ֲ�������ʾ��`Entity`��`Criteria`���ַ�ʽ��

###insert����
    public void insert() {
        User user = new User();
        user.setUserName("liyd");
        user.setUserAge(20);
        user.setGmtCreate(new Date());
        Long id = jdbcDao.insert(user);
        System.out.println(id);
    }
    
	public void insert2() {
		Criteria criteria = Criteria.create(User.class).set("userName", "liyd22")
			.set("userAge", 22).set("gmtCreate", new Date());
		Long id = jdbcDao.insert(criteria);
		System.out.println(id);
    }
    
###save��������insert���������ڲ������������ɵ�����ָ��
    public void save() {
        user.setUserId(-123L);
        jdbcDao.save(user);
    }
    
	public void save2() {
        Criteria criteria = Criteria.create(User.class).set("userId", -122L)
            .set("userName", "liyd22").set("userAge", 22).set("gmtCreate", new Date())
            .set("gmtModify", new Date());
        jdbcDao.save(criteria);
    }
    
###update����
    public void update() {
        user.setUserId(34L);
        user.setUserName("liyd34");
        user.setGmtCreate(null);
        user.setGmtModify(new Date());
        jdbcDao.update(user);
    }
    
	public void update2() {
        //����where����userId�����˶��ֵ��������in���
        Criteria criteria = Criteria.create(User.class).set("userName", "liydCriteria")
            .set("userAge", "18").where("userId", new Object[] { 34L, 33L, 32L });
        jdbcDao.update(criteria);
    }
    
###get����
    public void get1() {
        //��������
        User u = jdbcDao.get(User.class, 23L);
    }
    
	public void get2() {
        //criteria����Ҫ����ָ���ֶΰ���������������
        Criteria criteria = Criteria.create(User.class).include("userName");
        User u = jdbcDao.get(criteria, 23L);
    }

###delete����
	public void delete() {
        //��Ѳ�Ϊ�յ�������Ϊwhere����
        User u = new User();
        u.setUserName("selfly");
        u.setUserAge(16);
        jdbcDao.delete(u);
    }

    public void delete2() {
        //where����ʹ����or
        Criteria criteria = Criteria.create(User.class).where("userName", new Object[] { "liyd2" })
            .or("userAge", new Object[]{64});
        jdbcDao.delete(criteria);
    }

    public void delete3() {
        //��������
        jdbcDao.delete(User.class, 25L);
    }

###�б��ѯ����
	public void queryList1() {
        //�Բ�Ϊ�յ�������Ϊ��ѯ����
        User u = new User();
        u.setUserName("liyd");
        List<User> users = jdbcDao.queryList(u);
    }

    public void queryList2() {
        //Criteria��ʽ
        Criteria criteria = Criteria.create(User.class).exclude("userId")
            .where("userName", new Object[]{"liyd"});
        List<User> users = jdbcDao.queryList(criteria);
    }

    public void queryList3() {
        //ʹ����like�����Ի���!=��in��not in��
        Criteria criteria = Criteria.create(User.class).where("userName", "like",
            new Object[] { "%liyd%" });
        user.setUserAge(16);
        //����entity��criteria��ʽ���ʹ���ˣ���������
        List<User> users = jdbcDao.queryList(user, criteria.include("userId"));
    }

    public void queryList4() {
        //��ָ����������ѯ������
        List<User> users = jdbcDao.queryList(Criteria.create(User.class));
    }

###count��¼����ѯ�����˷���ֵ��һ���⣬�������б��ѯһ��
    public void queryCount() {
        user.setUserName("liyd");
        int count = jdbcDao.queryCount(user);
    }

    public void queryCount2() {
        Criteria criteria = Criteria.create(User.class).where("userName", new Object[] { "liyd" })
            .or("userAge", new Object[]{27});
        int count = jdbcDao.queryCount(criteria);
    }

###��ѯ�������
    public void querySingleResult() {
        user = jdbcDao.querySingleResult(user);
    }

    public void querySingleResult2() {
        Criteria criteria = Criteria.create(User.class).where("userName", new Object[] { "liyd" })
            .and("userId", new Object[]{23L});
        User u = jdbcDao.querySingleResult(criteria);
    }

###ָ���ֶΰ����������κβ�ѯ�����ж�����ʹ��
    public void get(){
        //��ֻ����userName
        Criteria criteria = Criteria.create(User.class).include("userName");
        User u = jdbcDao.get(criteria, 23L);
    }
    
###ָ���ֶκ����������κβ�ѯ�����ж�����ʹ��
	public void get4(){
        //��������userName
        Criteria criteria = Criteria.create(User.class).exclude("userName");
        User u = jdbcDao.get(criteria, 23L);
    }

###ָ������
    public void queryList() {
        //ָ����������ֶΣ�asc��desc
        Criteria criteria = Criteria.create(User.class).exclude("userId")
            .where("userName", new Object[]{"liyd"}).asc("userId").desc("userAge");
        List<User> users = jdbcDao.queryList(criteria);
    }

###��ҳ
	public void queryList1() {
		//���з�ҳ
		PageControl.performPage(user);
		//��ҳ��÷���������null����PageControl�л�ȡ
		jdbcDao.queryList(user);
		Pager pager = PageControl.getPager();
		//�б�
		List<User> users = pager.getList(User.class);
		//�ܼ�¼��
		int itemsTotal = pager.getItemsTotal();
	}

	public void queryList2() {
		//ֱ�Ӵ���ҳ���ÿҳ����
		PageControl.performPage(1, 10);
		//ʹ��Criteria��ʽ����ָ�������ֶη�ʽΪasc
		Criteria criteria = Criteria.create(User.class).include("userName", "userId")
			.where("userName", new Object[]{"liyd"}).asc("userId");
		jdbcDao.queryList(criteria);
		Pager pager = PageControl.getPager();
	}
	
###һЩ˵��

JdbcDao������ʱ���Ը�����Ҫע����������������

   <pre>`<bean id="jdbcDao" class="com.dexcoder.assistant.persistence.JdbcDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
        <property name="nameHandler" ref="..."/>
        <property name="rowMapperClass" value="..."/>
        <property name="dialect" value="..."/>
    </bean>`</pre>
    
- nameHandler Ĭ��ʹ��DefaultNameHandler�������������Լ���������ã������Ҫ�Զ������ʵ�ָýӿڡ�
- rowMapperClass Ĭ��ʹ����spring��`BeanPropertyRowMapper.newInstance(clazz)`,��Ҫ�Զ����������ʵ�֣���׼spring��RowMapperʵ�ּ��ɡ�
- dialect Ĭ��Ϊ������������MySql������������ָ�����ݿ���oracle

##�������

��Ӧ��ʵ��˼·�����裺http://www.dexcoder.com/blog/series/56.shtml

ĳЩ��ϸ��Ϣ������Ҫ�Ķ���һ���汾��http://www.dexcoder.com/blog/series/32.shtml

����QQ�� 341470  

�������䣺 javaer@live.com

����QQȺ�� 32261424





