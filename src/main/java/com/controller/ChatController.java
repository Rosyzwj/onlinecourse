package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.ChatEntity;
import com.entity.view.ChatView;
import com.service.ChatService;
import com.utils.AIUitl;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AI问答
 * 后端接口
 * @author
 * @email
 * @date 2023-03-06 09:56:36
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
	@Autowired
	private ChatService chatService;

	/**
	 * 后端列表
	 */
	@RequestMapping("/page")
	public R page(@RequestParam Map<String, Object> params,ChatEntity chat,
				  HttpServletRequest request){

		EntityWrapper<ChatEntity> ew = new EntityWrapper<ChatEntity>();

		PageUtils page = chatService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chat), params), params));
		return R.ok().put("data", page);
	}

	/**
	 * 前端列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params,ChatEntity chat,
				  HttpServletRequest request){
		if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
			chat.setUserid((Long)request.getSession().getAttribute("userId"));
		}
		EntityWrapper<ChatEntity> ew = new EntityWrapper<ChatEntity>();

		PageUtils page = chatService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chat), params), params));
		return R.ok().put("data", page);
	}

	/**
	 * 列表
	 */
	@RequestMapping("/lists")
	public R list( ChatEntity chat){
		EntityWrapper<ChatEntity> ew = new EntityWrapper<ChatEntity>();
		ew.allEq(MPUtil.allEQMapPre( chat, "chat"));
		return R.ok().put("data", chatService.selectListView(ew));
	}

	/**
	 * 查询
	 */
	@RequestMapping("/query")
	public R query(ChatEntity chat){
		EntityWrapper< ChatEntity> ew = new EntityWrapper< ChatEntity>();
		ew.allEq(MPUtil.allEQMapPre( chat, "chat"));
		ChatView chatView =  chatService.selectView(ew);
		return R.ok("查询AI问答成功").put("data", chatView);
	}

	/**
	 * 后端详情
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		ChatEntity chat = chatService.selectById(id);
		return R.ok().put("data", chat);
	}

	/**
	 * 前端详情
	 */
	@IgnoreAuth
	@RequestMapping("/detail/{id}")
	public R detail(@PathVariable("id") Long id){
		ChatEntity chat = chatService.selectById(id);
		return R.ok().put("data", chat);
	}




	/**
	 * 后端保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody ChatEntity chat, HttpServletRequest request){
		chat.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
		//ValidatorUtils.validateEntity(chat);
		if(StringUtils.isNotBlank(chat.getAsk())) {
			chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", request.getSession().getAttribute("userId")));
			chat.setUserid((Long)request.getSession().getAttribute("userId"));
			chat.setIsreply(1);
		}
		if(StringUtils.isNotBlank(chat.getReply())) {
			chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", chat.getUserid()));
			chat.setAdminid((Long)request.getSession().getAttribute("userId"));
		}
		chatService.insert(chat);
		return R.ok();
	}

	/**
	 * 前端保存
	 */
	@RequestMapping("/add")
	public R add(@RequestBody ChatEntity chat, HttpServletRequest request){
		chat.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
		//ValidatorUtils.validateEntity(chat);
		chat.setUserid((Long)request.getSession().getAttribute("userId"));
		if(StringUtils.isNotBlank(chat.getAsk())) {
			chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", request.getSession().getAttribute("userId")));
			chat.setUserid((Long)request.getSession().getAttribute("userId"));
			chat.setIsreply(1);
		}
		if(StringUtils.isNotBlank(chat.getReply())) {
			chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", chat.getUserid()));
			chat.setAdminid((Long)request.getSession().getAttribute("userId"));
		}
		chatService.insert(chat);
		return R.ok();
	}


	/**
	 * AI问答接口（带历史上下文，同步返回）
	 */
	@RequestMapping("/aichat")
	public R aichat(@RequestParam String question, HttpServletRequest request) {
		try {
			if (StringUtils.isBlank(question)) {
				return R.error("问题不能为空");
			}
			Long userId = (Long) request.getSession().getAttribute("userId");
			if (userId == null) {
				return R.error("请先登录");
			}

			// 查询该用户最近 5 条历史记录作为上下文
			List<AIUitl.HistoryMessage> history = getRecentHistory(userId, 5);

			String answer = AIUitl.getResponseWithHistory(history, question);

			ChatEntity chat = new ChatEntity();
			chat.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
			chat.setUserid(userId);
			chat.setAdminid(1L);
			chat.setAsk(question);
			chat.setReply(answer);
			chat.setIsreply(1);
			chat.setAddtime(new Date());
			chatService.insert(chat);

			return R.ok()
					.put("question", question)
					.put("answer", answer)
					.put("chatId", chat.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("AI问答服务异常：" + e.getMessage());
		}
	}

	/**
	 * AI问答流式接口（SSE），逐字推送 AI 回答
	 * 前端通过 EventSource 接收，实现打字机效果
	 */
	@GetMapping(value = "/aichat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter aichatStream(@RequestParam String question, HttpServletRequest request) {
		Long userId = (Long) request.getSession().getAttribute("userId");

		// SseEmitter 超时设置为 3 分钟
		SseEmitter emitter = new SseEmitter(180_000L);

		if (StringUtils.isBlank(question)) {
			try {
				emitter.send(SseEmitter.event().name("error").data("问题不能为空"));
				emitter.complete();
			} catch (IOException ignored) {}
			return emitter;
		}
		if (userId == null) {
			try {
				emitter.send(SseEmitter.event().name("error").data("请先登录"));
				emitter.complete();
			} catch (IOException ignored) {}
			return emitter;
		}

		List<AIUitl.HistoryMessage> history = getRecentHistory(userId, 5);
		final Long finalUserId = userId;
		final StringBuilder fullAnswer = new StringBuilder();

		// 在新线程中执行流式调用，避免阻塞 Servlet 线程
		Thread thread = new Thread(() -> {
			try {
				AIUitl.streamResponseWithHistory(history, question, text -> {
					try {
						fullAnswer.append(text);
						emitter.send(SseEmitter.event().name("message").data(text));
					} catch (IOException e) {
						emitter.completeWithError(e);
					}
				});

				// 流结束后保存完整对话到数据库
				ChatEntity chat = new ChatEntity();
				chat.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
				chat.setUserid(finalUserId);
				chat.setAdminid(1L);
				chat.setAsk(question);
				chat.setReply(fullAnswer.toString());
				chat.setIsreply(1);
				chat.setAddtime(new Date());
				chatService.insert(chat);

				// 发送结束事件，携带 chatId 供前端使用
				emitter.send(SseEmitter.event().name("done").data(chat.getId()));
				emitter.complete();
			} catch (Exception e) {
				try {
					emitter.send(SseEmitter.event().name("error").data("AI问答服务异常：" + e.getMessage()));
				} catch (IOException ignored) {}
				emitter.completeWithError(e);
			}
		});
		thread.setDaemon(true);
		thread.start();

		return emitter;
	}

	/**
	 * 查询指定用户最近 N 条已回复的历史记录，用于构建上下文
	 */
	private List<AIUitl.HistoryMessage> getRecentHistory(Long userId, int limit) {
		EntityWrapper<ChatEntity> ew = new EntityWrapper<>();
		ew.eq("userid", userId).eq("isreply", 1).orderBy("addtime", false);
		List<ChatEntity> records = chatService.selectList(ew);

		// 取最近 limit 条，并按时间正序排列（旧 → 新）
		int start = Math.max(0, records.size() - limit);
		List<ChatEntity> recent = records.subList(start, records.size());

		List<AIUitl.HistoryMessage> history = new ArrayList<>();
		for (ChatEntity r : recent) {
			history.add(new AIUitl.HistoryMessage(r.getAsk(), r.getReply()));
		}
		return history;
	}
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@Transactional
	public R update(@RequestBody ChatEntity chat, HttpServletRequest request){
		//ValidatorUtils.validateEntity(chat);
		chatService.updateById(chat);//全部更新
		return R.ok();
	}




	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids){
		chatService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 提醒接口
	 */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);

		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}

		Wrapper<ChatEntity> wrapper = new EntityWrapper<ChatEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = chatService.selectCount(wrapper);
		return R.ok().put("count", count);
	}










}
